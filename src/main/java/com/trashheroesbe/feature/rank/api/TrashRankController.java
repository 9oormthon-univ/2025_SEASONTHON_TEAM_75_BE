package com.trashheroesbe.feature.rank.api;

import com.trashheroesbe.feature.rank.application.TrashRankService;
import com.trashheroesbe.feature.rank.dto.response.WeeklyRankResponse;
import com.trashheroesbe.global.response.ApiResponse;
import com.trashheroesbe.global.response.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rank")
public class TrashRankController implements TrashRankControllerApi {

    private final TrashRankService trashRankService;

    @Override
    @GetMapping("/current")
    public ApiResponse<WeeklyRankResponse> getCurrentWeekRanking() {
        WeeklyRankResponse response = trashRankService.getCurrentWeekRanking();
        return ApiResponse.success(SuccessCode.OK, response);
    }
}
