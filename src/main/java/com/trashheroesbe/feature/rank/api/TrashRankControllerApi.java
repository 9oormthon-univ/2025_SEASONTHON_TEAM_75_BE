package com.trashheroesbe.feature.rank.api;

import com.trashheroesbe.feature.rank.dto.response.WeeklyRankResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;

public interface TrashRankControllerApi {

    @Operation(summary = "현재 주간 랭킹 조회", description = "현재 주간의 쓰레기 검색 랭킹을 조회합니다.(매주 월요일 update)")
    ApiResponse<WeeklyRankResponse> getCurrentWeekRanking();
}
