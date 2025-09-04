package com.trashheroesbe.feature.disposal.api;

import com.trashheroesbe.feature.disposal.dto.response.DistrictTrashDisposalResponse;
import com.trashheroesbe.feature.user.dto.response.UserDistrictResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Disposal", description = "자치구별 배출 관련 API")
public interface DisposalControllerApi {

    @Operation(summary = "오늘의 분리수거 조회", description = "나의 등록된 자치구의 분리수거 정보를 조회 합니다.")
    ApiResponse<List<DistrictTrashDisposalResponse>> getTodayTrashDisposal(
        CustomerDetails customerDetails
    );
}
