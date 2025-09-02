package com.trashheroesbe.feature.gpt.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trashheroesbe.feature.gpt.dto.response.TrashAnalysisResponseDto;
import com.trashheroesbe.feature.trash.domain.TrashType;
import com.trashheroesbe.feature.trash.domain.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatGPTClient {

    private final ChatClient chatClient;
    private final ObjectMapper om = new ObjectMapper();

    public ChatGPTClient(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // 1단계: 타입 + 설명
    public TrashAnalysisResponseDto analyzeType(byte[] imageBytes, String contentType) {
        try {
            MimeType mime = (contentType != null && contentType.startsWith("image/"))
                    ? MimeType.valueOf(contentType)
                    : MimeTypeUtils.IMAGE_JPEG;

            ByteArrayResource resource = new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    mime.getSubtype();
                    String subtype = mime.getSubtype();
                    return "upload." + subtype;
                }
            };

            String content = chatClient
                    .prompt()
                    .system(buildTypePrompt())
                    .user(u -> u.text("이 이미지를 분류해 JSON만 반환해줘.").media(mime, resource))
                    .call()
                    .content();

            return parseTypeResponse(content);

        } catch (Exception e) {
            log.error("1단계(type) 분석 실패", e);
            return TrashAnalysisResponseDto.of(TrashType.of(Type.UNKNOWN), null, "이미지 분석에 실패했습니다.");
        }
    }

    // 2단계: 세부 품목(item)만
    public String analyzeItem(byte[] imageBytes, String contentType, Type type) {
        try {
            MimeType mime = (contentType != null && contentType.startsWith("image/"))
                    ? MimeType.valueOf(contentType)
                    : MimeTypeUtils.IMAGE_JPEG;

            ByteArrayResource resource = new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    mime.getSubtype();
                    String subtype = mime.getSubtype();
                    return "upload." + subtype;
                }
            };

            String content = chatClient
                    .prompt()
                    .system(buildItemPrompt(type))
                    .user(u -> u.text("이 이미지를 분류해 JSON만 반환해줘.").media(mime, resource))
                    .call()
                    .content();

            return parseItemResponse(content);

        } catch (Exception e) {
            log.error("2단계(item) 분석 실패", e);
            return null;
        }
    }

    private String buildTypePrompt() {
        String allowed = java.util.Arrays.stream(Type.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        return """
            너는 쓰레기 분류 전문가다. 아래 규칙을 엄격히 지켜라.
            - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
            - type 값은 다음 중 정확히 하나만 허용: %s
            - description은 한국어 50자 내.
            - 출력 형식: {"type":"PAPER","description":"..."}
            """.formatted(allowed);
    }

    private final Map<Type, List<String>> ALLOWED_ITEMS = new EnumMap<>(Type.class) {{
        put(Type.PLASTIC, List.of("PET(투명 페트병)", "PET(유색 페트병)", "플라스틱 병", "음식 용기", "과일용기", "샴푸병"));
        put(Type.GLASS, List.of("투명 유리병", "유색 유리병"));
        put(Type.METAL, List.of("알루미늄 캔", "철 캔"));
        put(Type.PAPER, List.of("일반 종이류", "종이팩"));
        put(Type.VINYL_FILM, List.of("비닐봉투", "뽁뽁이", "아이스팩"));
        put(Type.STYROFOAM, List.of("완충 포장재", "라면용기", "식품 포장상자", "아이스박스"));
        put(Type.TEXTILES, List.of("의류", "섬유"));
        put(Type.E_WASTE, List.of("냉장고", "TV", "핸드폰", "라디오"));
        put(Type.HAZARDOUS_SMALL_WASTE, List.of("폐형광등", "폐건전지", "보조배터리"));
    }};

    private String buildItemPrompt(Type type) {
        List<String> items = ALLOWED_ITEMS.getOrDefault(type, List.of());
        String list = items.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));
        return """
            너는 쓰레기 분류 전문가다. 세부 품목(item)만 판단하여 JSON으로 반환하라.
            - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
            - type=%s 이며, item은 아래 허용 목록 중 정확히 하나(한국어)만 선택:
            [ %s ]
            - 출력 형식: {"item":"PET(투명 페트병)"}
            """.formatted(type.name(), list);
    }

    private TrashAnalysisResponseDto parseTypeResponse(String content) {
        try {
            if (content == null) {
                return TrashAnalysisResponseDto.of(TrashType.of(Type.UNKNOWN), null, "이미지 분석에 실패했습니다.");
            }

            String json = sanitizeToJson(content);
            JsonNode node = om.readTree(json);

            String typeStr = node.path("type").asText("UNKNOWN");
            String description = node.path("description").asText("설명을 생성하지 못했습니다.");

            Type typeEnum;
            try {
                typeEnum = Type.valueOf(typeStr.trim().toUpperCase());
            } catch (Exception e) {
                typeEnum = Type.UNKNOWN;
            }

            return TrashAnalysisResponseDto.of(TrashType.of(typeEnum), null, description);

        } catch (Exception e) {
            log.error("1단계 응답 파싱 실패", e);
            return TrashAnalysisResponseDto.of(TrashType.of(Type.UNKNOWN), null, "이미지 분석에 실패했습니다.");
        }
    }

    private String parseItemResponse(String content) {
        try {
            if (content == null) return null;
            String json = sanitizeToJson(content);
            JsonNode node = om.readTree(json);
            String item = node.path("item").asText(null);
            if (item != null && !item.isBlank()) return item.trim();
            return null;
        } catch (Exception e) {
            log.error("2단계 응답 파싱 실패", e);
            return null;
        }
    }

    private String sanitizeToJson(String content) {
        String trimmed = content.trim();
        int s = trimmed.indexOf('{');
        int e = trimmed.lastIndexOf('}');
        if (s >= 0 && e > s) return trimmed.substring(s, e + 1);
        return trimmed;
    }
}