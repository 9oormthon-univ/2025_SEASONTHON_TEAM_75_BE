package com.trashheroesbe.feature.rank.dto.response;

import com.trashheroesbe.feature.rank.domain.TrashRank;
import com.trashheroesbe.feature.rank.domain.TrendDirection;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TrashRankResponse(
    Long rankId,
    String trashImageUrl,
    String trashTypeName,
    LocalDate weekStartDate,
    LocalDate weekEndDate,
    Integer rankOrder,
    Integer previousRank,
    Integer searchCount,
    Integer previousSearchCount,
    TrendDirection trendDirection,
    String trendMessage,
    Integer rankChange
) {

    public static TrashRankResponse from(TrashRank trashRank) {
        Integer rankChange = calculateRankChange(
            trashRank.getRankOrder(), trashRank.getPreviousRank());

        return TrashRankResponse.builder()
            .rankId(trashRank.getRankId())
            .trashTypeName(trashRank.getTrashType().getType().getNameKo())
            .weekStartDate(trashRank.getWeekStartDate())
            .weekEndDate(trashRank.getWeekEndDate())
            .rankOrder(trashRank.getRankOrder())
            .previousRank(trashRank.getPreviousRank())
            .searchCount(trashRank.getSearchCount())
            .previousSearchCount(trashRank.getPreviousSearchCount())
            .trendDirection(trashRank.getTrendDirection())
            .trendMessage(trashRank.getTrendDirection() != null ?
                trashRank.getTrendDirection().getMessage() : null)
            .rankChange(rankChange)
            .build();
    }

    private static Integer calculateRankChange(Integer currentRank, Integer previousRank) {
        if (previousRank == null) {
            return null;
        }
        return previousRank - currentRank;
    }
}
