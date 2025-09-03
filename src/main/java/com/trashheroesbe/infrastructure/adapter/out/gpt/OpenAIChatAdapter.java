package com.trashheroesbe.infrastructure.adapter.out.gpt;

import static com.trashheroesbe.global.response.type.ErrorCode.EMPTY_GPT_RESPONSE;
import static com.trashheroesbe.global.response.type.ErrorCode.ERROR_GPT_CALL;
import static com.trashheroesbe.global.response.type.ErrorCode.FAIL_PARSING_RESPONSE;
import static com.trashheroesbe.global.response.type.ErrorCode.INVALID_SEARCH_KEYWORD;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trashheroesbe.feature.trash.dto.response.TrashAnalysisResponseDto;
import com.trashheroesbe.feature.trash.domain.Type;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.infrastructure.port.gpt.ChatAIClientPort;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Service
@Slf4j
public class OpenAIChatAdapter implements ChatAIClientPort {

    private final ObjectMapper om = new ObjectMapper();
    private final ChatClient chatClient;

    public OpenAIChatAdapter(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public TrashAnalysisResponseDto analyzeType(byte[] imageBytes, String contentType) {
        try {
            MimeType mime = (contentType != null && contentType.startsWith("image/"))
                ? MimeType.valueOf(contentType) : MimeTypeUtils.IMAGE_JPEG;

            ByteArrayResource resource = new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    String subtype = mime.getSubtype();
                    return "upload." + subtype;
                }
            };

            String content = chatClient.prompt()
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

    @Override
    public String analyzeItem(byte[] imageBytes, String contentType, Type type) {
        try {
            MimeType mime = (contentType != null && contentType.startsWith("image/"))
                ? MimeType.valueOf(contentType) : MimeTypeUtils.IMAGE_JPEG;

            ByteArrayResource resource = new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    String subtype = mime.getSubtype();
                    return "upload." + subtype;
                }
            };

            String content = chatClient.prompt()
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

    @Override
    public Type findSimilarTrashItem(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessException(INVALID_SEARCH_KEYWORD);
        }
        String prompt = buildSimilarTrashTypePrompt(keyword);

        try {
            String gptResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

            return parseSimilarTypeResponse(gptResponse);

        } catch (Exception e) {
            throw new BusinessException(ERROR_GPT_CALL);
        }
    }

    private String buildSimilarTrashTypePrompt(String keyword) {
        String availableTypes = ALLOWED_ITEMS.keySet().stream()
            .map(Type::name)
            .collect(Collectors.joining(", "));

        return String.format("""
            너는 쓰레기 분류 전문가다. 다음 키워드가 어떤 쓰레기 분류에 해당하는지 판단해라.
            아래 규칙을 엄격히 지켜라.
             - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
             - 검색 키워드는: "%s"
             - type 값은 다음 중 정확히 하나만 허용: %s
             - 출력 형식: {"type": "PLASTIC"}
             - 만약 해당하는 타입이 없다면 {"type": "NONE"}으로 응답해라.
            답변:""", keyword, availableTypes);
    }

    private Type parseSimilarTypeResponse(String gptResponse) {
        if (gptResponse == null || gptResponse.trim().isEmpty()) {
            throw new BusinessException(EMPTY_GPT_RESPONSE);
        }
        try {
            String json = sanitizeToJson(gptResponse);
            JsonNode node = om.readTree(json);
            String typeStr = node.path("type").asText("UNKNOWN");

            if ("NONE".equalsIgnoreCase(typeStr) || "UNKNOWN".equalsIgnoreCase(typeStr)) {
                return null;
            }
            return Type.valueOf(typeStr.toUpperCase());
        } catch (JsonProcessingException e) {
            throw new BusinessException(FAIL_PARSING_RESPONSE);
        }
    }

    private String buildTypePrompt() {
        String allowed = java.util.Arrays.stream(Type.values())
            .map(Enum::name).collect(Collectors.joining(", "));
        return """
            너는 쓰레기 분류 전문가다. 아래 규칙을 엄격히 지켜라.
            - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
            - type 값은 다음 중 정확히 하나만 허용: %s
            - description은 한국어 50자 내.
            - 출력 형식: {"type":"PAPER","description":"..."}
            """.formatted(allowed);
    }

    private final Map<Type, List<String>> ALLOWED_ITEMS = new EnumMap<>(Type.class) {{
        put(Type.PAPER, List.of("일반 종이류"));
        put(Type.PAPER_PACK, List.of("종이팩"));
        put(Type.PLASTIC, List.of("플라스틱 병", "음식 용기", "과일용기", "샴푸병"));
        put(Type.PET, List.of("PET(투명 페트병)", "PET(유색 페트병)"));
        put(Type.VINYL_FILM, List.of("비닐봉투", "뽁뽁이", "아이스팩"));
        put(Type.STYROFOAM, List.of("완충 포장재", "라면용기", "식품 포장상자", "아이스박스"));
        put(Type.GLASS, List.of("투명 유리병", "유색 유리병"));
        put(Type.METAL, List.of("알루미늄 캔", "철 캔"));
        put(Type.TEXTILES, List.of("의류", "섬유"));
        put(Type.E_WASTE, List.of("냉장고", "TV", "핸드폰", "라디오"));
        put(Type.HAZARDOUS_SMALL_WASTE, List.of("폐형광등", "폐건전지", "보조배터리"));
        put(Type.FOOD_WASTE, List.of("야채,과일 껍질", "남은음식", "뼈(닭 등의 뼈다귀, 생선뼈)", "껍데기(갑각류, 어패류)"));
        put(Type.NON_RECYCLABLE, List.of("일반쓰레기", "기름이 묻은 종이", "카드영수증", "부서진 그릇"));
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
                return TrashAnalysisResponseDto.of(TrashType.of(Type.UNKNOWN), null,
                    "이미지 분석에 실패했습니다.");
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
            if (content == null) {
                return null;
            }
            String json = sanitizeToJson(content);
            JsonNode node = om.readTree(json);
            String item = node.path("item").asText(null);
            if (item != null && !item.isBlank()) {
                return item.trim();
            }
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
        if (s >= 0 && e > s) {
            return trimmed.substring(s, e + 1);
        }
        return trimmed;
    }
}