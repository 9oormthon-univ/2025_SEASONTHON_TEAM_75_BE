package com.trashheroesbe.feature.district.api;

import com.trashheroesbe.feature.district.dto.response.DistrictListResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "District", description = "자치구 관련 API")
public interface DistrictControllerApi {

    @Operation(summary = "자치구 정보 검색하기", description = "시도, 시군구를 통해 자치구 정보를 검색한다.")
    ApiResponse<List<DistrictListResponse>> getDistrictList(
        @Parameter(description = "검색할 시도", example = "서울특별시")
        String sido,

        @Parameter(description = "검색할 시군구", example = "마포구")
        String sigungu
    );
}
