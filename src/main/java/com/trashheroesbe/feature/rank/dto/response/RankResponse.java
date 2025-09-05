package com.trashheroesbe.feature.rank.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;


@Builder
@Schema(description = "검색 랭킹 전체 응답")
public record RankResponse(

    @Schema(description = "랭킹 목록", implementation = TrashRankResponse.class)
    List<TrashRankResponse> rankings,

    @Schema(description = "총 랭킹 개수", example = "10")
    Integer totalCount,

    @Schema(description = "마지막 업데이트 시간", example = "2025-09-06T12:00:00")
    LocalDateTime lastUpdated
) {

    public static RankResponse of(
        List<TrashRankResponse> rankings,
        LocalDateTime lastUpdated
    ) {
        return RankResponse.builder()
            .rankings(rankings)
            .totalCount(rankings.size())
            .lastUpdated(lastUpdated)
            .build();
    }
}
