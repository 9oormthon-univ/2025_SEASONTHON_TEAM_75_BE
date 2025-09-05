package com.trashheroesbe.feature.rank.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record WeeklyRankResponse(
    LocalDate weekStartDate,
    LocalDate weekEndDate,
    List<TrashRankResponse> rankings,
    Integer totalCount
) {

    public static WeeklyRankResponse of(
        LocalDate weekStartDate,
        LocalDate weekEndDate,
        List<TrashRankResponse> rankings
    ) {
        return WeeklyRankResponse.builder()
            .weekStartDate(weekStartDate)
            .weekEndDate(weekEndDate)
            .rankings(rankings)
            .totalCount(rankings.size())
            .build();
    }
}
