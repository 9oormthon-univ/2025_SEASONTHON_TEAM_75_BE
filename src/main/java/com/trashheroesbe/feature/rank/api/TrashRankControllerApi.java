package com.trashheroesbe.feature.rank.api;

import com.trashheroesbe.feature.rank.dto.response.RankResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

public interface TrashRankControllerApi {

    @Operation(summary = "현재 랭킹 조회", description = "현재 누적된 쓰레기 검색 랭킹을 조회합니다.(3시간 마다 update)")
    ApiResponse<RankResponse> getCurrentRanking();
}
