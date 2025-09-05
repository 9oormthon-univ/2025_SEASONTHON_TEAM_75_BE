package com.trashheroesbe.feature.rank.dto.response;

import com.trashheroesbe.feature.rank.domain.TrashRank;
import com.trashheroesbe.feature.rank.domain.TrendDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TrashRankResponse(
    @Schema(description = "랭킹 ID", example = "1")
    Long rankId,

    @Schema(description = "쓰레기 이미지 URL", example = "https://cdn.trashheroes.store/images/plastic.png")
    String trashImageUrl,

    @Schema(description = "쓰레기 종류 이름(한글)", example = "플라스틱류")
    String trashTypeName,

    @Schema(description = "현재 순위", example = "1")
    Integer rankOrder,

    @Schema(description = "이전 순위", example = "2")
    Integer previousRank,

    @Schema(description = "총 누적 검색 수", example = "15230")
    Integer totalSearchCount,

    @Schema(description = "이전 주차까지 누적 검색 수", example = "14707")
    Integer previousTotalSearchCount,

    @Schema(description = "트렌드 방향 (UP: 상승, DOWN: 하락, SAME: 동일)", example = "UP")
    TrendDirection trendDirection,

    @Schema(description = "트렌드 메시지", example = "순위가 올랐습니다")
    String trendMessage,

    @Schema(description = "주간 검색 수 변화량 (이번주 - 지난주)", example = "523")
    Integer countChange,

    @Schema(description = "순위 변화량 (이전순위 - 현재순위)", example = "1")
    Integer rankChange
) {

    public static TrashRankResponse from(TrashRank trashRank) {
        Integer rankChange = trashRank.getPreviousRank() - trashRank.getRankOrder();
        Integer countChange = trashRank.getSearchCount() - trashRank.getPreviousSearchCount();
        return TrashRankResponse.builder()
            .rankId(trashRank.getRankId())
            .trashImageUrl(trashRank.getTrashType().getImageUrl())
            .trashTypeName(trashRank.getTrashType().getType().getNameKo())
            .rankOrder(trashRank.getRankOrder())
            .previousRank(trashRank.getPreviousRank())
            .totalSearchCount(trashRank.getSearchCount())
            .previousTotalSearchCount(trashRank.getPreviousSearchCount())
            .trendDirection(trashRank.getTrendDirection())
            .trendMessage(trashRank.getTrendDirection() != null ?
                trashRank.getTrendDirection().getMessage() : null)
            .countChange(countChange)
            .rankChange(rankChange)
            .build();
    }
}
