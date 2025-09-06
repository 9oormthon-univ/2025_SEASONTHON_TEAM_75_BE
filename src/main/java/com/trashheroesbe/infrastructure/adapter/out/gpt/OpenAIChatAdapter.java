package com.trashheroesbe.infrastructure.adapter.out.gpt;

import static com.trashheroesbe.global.response.type.ErrorCode.EMPTY_GPT_RESPONSE;
import static com.trashheroesbe.global.response.type.ErrorCode.ERROR_GPT_CALL;
import static com.trashheroesbe.global.response.type.ErrorCode.FAIL_PARSING_RESPONSE;
import static com.trashheroesbe.global.response.type.ErrorCode.INVALID_SEARCH_KEYWORD;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trashheroesbe.feature.trash.dto.response.TrashAnalysisResponseDto;
import com.trashheroesbe.feature.trash.domain.type.Type;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.infrastructure.TrashItemRepository;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.infrastructure.adapter.out.gpt.dto.SimilarResult;
import com.trashheroesbe.infrastructure.port.gpt.ChatAIClientPort;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import com.trashheroesbe.feature.trash.domain.entity.TrashItem;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAIChatAdapter implements ChatAIClientPort {

    private final ObjectMapper om;
    private final ChatClient chatClient;
    private final TrashItemRepository trashItemRepository;

    private final Map<Type, List<String>> itemCache = new ConcurrentHashMap<>();

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
    public String suggestNameByImage(byte[] imageBytes, String contentType, Type type) {
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
                .system(buildNamePrompt(type))
                .user(u -> u.text("이 이미지를 보고 핵심 명칭을 요약해 JSON만 반환해줘.").media(mime, resource))
                .call()
                .content();

            return parseNameResponse(content);
        } catch (Exception e) {
            log.error("이름 요약(이미지) 실패", e);
            return null;
        }
    }

    @Override
    public String suggestNameByKeyword(String keyword, Type type) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        try {
            String prompt = buildNameByKeywordPrompt(keyword, type);
            String content = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
            return parseNameResponse(content);
        } catch (Exception e) {
            log.error("이름 요약(키워드) 실패", e);
            return null;
        }
    }

    private String buildNameByKeywordPrompt(String keyword, Type type) {
        return """
            너는 쓰레기 명칭 요약 전문가다.
            - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
            - name은 한국어 2~12자, 과도한 수식어/조사 제거.
            - type=%s, keyword="%s"
            - 출력 형식: {"name":"투명 페트병"}
            답변:""".formatted(type != null ? type.name() : "UNKNOWN", keyword);
    }

    private String parseNameResponse(String content) {
        try {
            if (content == null) {
                return null;
            }
            String json = sanitizeToJson(content);
            JsonNode node = om.readTree(json);
            String name = node.path("name").asText(null);
            if (name != null && !name.isBlank()) {
                return name.trim();
            }
            return null;
        } catch (Exception e) {
            log.error("이름 요약 응답 파싱 실패", e);
            return null;
        }
    }

    private String buildNamePrompt(Type type) {
        List<String> items = loadAllowedItems(type);
        if (items.isEmpty()) {
            return """
                너는 쓰레기 명칭 요약 전문가다.
                - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
                - name은 한국어 2~12자, 핵심 품목명 위주로 간결히.
                - type=%s
                - 출력 형식: {"name":"투명 페트병"}
                """.formatted(type != null ? type.name() : "UNKNOWN");
        }
        String list = items.stream().limit(30).map(s -> "\"" + s + "\"")
            .collect(java.util.stream.Collectors.joining(", "));
        return """
            너는 쓰레기 명칭 요약 전문가다.
            - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
            - name은 한국어 2~12자, 핵심 품목명 위주로 간결히.
            - type=%s, 아래 예시 품목을 참고해 가장 일반적 명칭으로 요약(그대로 복붙 금지):
            [ %s ]
            - 출력 형식: {"name":"%s"}
            """.formatted(type != null ? type.name() : "UNKNOWN", list, items.get(0));
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
    public SimilarResult findSimilarTrashItem(
        String keyword,
        List<String> itemNames,
        List<Type> types
    ) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessException(INVALID_SEARCH_KEYWORD);
        }
        String prompt = buildSimilarTrashTypePrompt(keyword, itemNames, types);

        try {
            String gptResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

            return parseSimilarItemNameResponse(gptResponse);

        } catch (Exception e) {
            throw new BusinessException(ERROR_GPT_CALL);
        }
    }

    private String buildSimilarTrashTypePrompt(
        String keyword,
        List<String> itemNames,
        List<Type> types
    ) {
        String availableItemNames = itemNames.stream()
            .filter(name -> !name.contains("기타"))
            .map(String::toString)
            .collect(Collectors.joining(", "));

        String availableTypes = types.stream()
            .map(Enum::name)
            .collect(Collectors.joining(", "));

        return String.format("""
            너는 쓰레기 분류 전문가다. 다음 키워드가 어떤 쓰레기 분류에 해당하는지 판단해라.
            아래 규칙을 엄격히 지켜라.
             - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
             - 검색 키워드는: "%s"
             - item 값은 다음 중 정확히 하나만 허용: %s
              - ,로 구분되는 전체 정확하게 출력해서 담아줘
             - 출력 형식: {"itemName": "어패류 껍데기"}
             - 만약 해당하는 타입이 없다면 {"itemName": "NONE"}으로 응답해라.
             - 만약 위에 item 으로 찾을 수 없다면 아래 목록을 보고 응답해라
             - type 값 은 다음 중 정확히 하나 만 허용: "%s"
             - 출력 형식: {"type": "STYROFOAM"}
            답변:""", keyword, availableItemNames, availableTypes);
    }

    private SimilarResult parseSimilarItemNameResponse(String gptResponse) {
        if (gptResponse == null || gptResponse.trim().isEmpty()) {
            throw new BusinessException(EMPTY_GPT_RESPONSE);
        }
        try {
            String json = sanitizeToJson(gptResponse);
            JsonNode node = om.readTree(json);

            String itemName = node.path("itemName").asText(null);
            if (itemName != null && !itemName.isBlank() && !"NONE".equalsIgnoreCase(itemName)) {
                return SimilarResult.ofItem(itemName);
            }

            String typeStr = node.path("type").asText(null);
            if (typeStr != null && !typeStr.isBlank()
                && !"NONE".equalsIgnoreCase(typeStr)
                && !"UNKNOWN".equalsIgnoreCase(typeStr)) {
                return SimilarResult.ofType(Type.valueOf(typeStr.toUpperCase()));
            }
            return SimilarResult.none();
        } catch (JsonProcessingException e) {
            throw new BusinessException(FAIL_PARSING_RESPONSE);
        }
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

    private List<String> loadAllowedItems(Type type) {
        return itemCache.computeIfAbsent(type, t ->
            trashItemRepository.findByTrashType_Type(t).stream()
                .map(TrashItem::getName)
                .filter(s -> s != null && !s.isBlank())
                .toList()
        );
    }

    private String buildItemPrompt(Type type) {
        List<String> items = loadAllowedItems(type);
        if (items.isEmpty()) {
            // 품목 테이블에 데이터가 없는 타입은 목록 제한 없이 간단 규칙만 제공
            return """
                너는 쓰레기 분류 전문가다. 세부 품목(item)만 판단하여 JSON으로 반환하라.
                - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
                - type=%s 이다. item은 한국어로 간결히 작성:
                - 출력 형식: {"item":"PET(투명 페트병)"}
                """.formatted(type.name());
        }
        String list = items.stream().map(s -> "\"" + s + "\"")
            .collect(java.util.stream.Collectors.joining(", "));
        return """
            너는 쓰레기 분류 전문가다. 세부 품목(item)만 판단하여 JSON으로 반환하라.
            - JSON(객체)만 반환. 코드블록, 여분 텍스트 금지.
            - type=%s 이며, item은 아래 허용 목록 중 정확히 하나(한국어)만 선택:
            [ %s ]
            - 출력 형식: {"item":"%s"}
            """.formatted(type.name(), list, items.get(0));
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