package com.trashheroesbe.feature.point.api;

import com.trashheroesbe.feature.point.domain.entity.UserPoint;
import com.trashheroesbe.feature.point.dto.response.UserPointResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Point", description = "포인트 관련 API")
public interface PointControllerApi {

    @Operation(summary = "내 포인트 조회", description = "내 포인트 정보를 조회합니다.")
    ApiResponse<UserPointResponse> getMyPoint(CustomerDetails customerDetails);
}
