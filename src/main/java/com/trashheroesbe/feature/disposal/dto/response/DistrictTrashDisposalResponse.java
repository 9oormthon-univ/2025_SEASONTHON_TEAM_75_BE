package com.trashheroesbe.feature.disposal.dto.response;

import com.trashheroesbe.feature.disposal.domain.Disposal;
import com.trashheroesbe.feature.trash.domain.type.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record DistrictTrashDisposalResponse(
    String categoryName,
    List<String> trashTypes,
    String location,
    String todayDay,
    LocalDate todayDate
) {

    public static List<DistrictTrashDisposalResponse> of(
        List<Disposal> disposals,
        String todayDay,
        LocalDate todayDate
    ) {
        if (disposals.isEmpty()) {
            return List.of();
        }

        String location = disposals.getFirst().getDistrict().getSigungu();

        // 카테고리별로 그룹핑
        Map<String, List<String>> grouped = disposals.stream()
            .collect(Collectors.groupingBy(
                disposal -> getCategoryName(disposal.getTrashType().getType()),
                Collectors.mapping(
                    disposal -> disposal.getTrashType().getType().getNameKo(),
                    Collectors.toList()
                )
            ));

        return grouped.entrySet().stream()
            .map(entry -> DistrictTrashDisposalResponse.builder()
                .categoryName(entry.getKey())
                .trashTypes(entry.getValue())
                .location(location)
                .todayDay(todayDay)
                .todayDate(todayDate)
                .build())
            .sorted((a, b) -> getCategoryOrder(a.categoryName) - getCategoryOrder(b.categoryName))
            .toList();
    }

    private static String getCategoryName(Type type) {
        return switch (type) {
            case NON_RECYCLABLE, FOOD_WASTE -> "일반/음식물쓰레기";
            case PET, VINYL_FILM -> "투명페트병/비닐";
            case PAPER, PAPER_PACK, PLASTIC, STYROFOAM, GLASS, METAL, TEXTILES,
                 E_WASTE, HAZARDOUS_SMALL_WASTE -> "그외 재활용품";
            default -> "기타";
        };
    }

    private static int getCategoryOrder(String categoryName) {
        return switch (categoryName) {
            case "일반쓰레기/음식물쓰레기" -> 1;
            case "투명페트병/비닐류" -> 2;
            case "그외 재활용품" -> 3;
            default -> 4;
        };
    }
}
