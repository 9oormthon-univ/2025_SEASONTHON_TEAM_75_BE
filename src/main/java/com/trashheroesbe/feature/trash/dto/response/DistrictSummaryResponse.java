package com.trashheroesbe.feature.trash.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 기본 자치구")
public record DistrictSummaryResponse(
        @Schema(description = "자치구 ID", example = "1100053")
        String id,
        @Schema(description = "시/도", example = "서울특별시")
        String sido,
        @Schema(description = "시/군/구", example = "강남구")
        String sigungu,
        @Schema(description = "읍/면/동", example = "역삼동")
        String eupmyeondong
) {}