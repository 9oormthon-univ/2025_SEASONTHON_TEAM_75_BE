package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.type.CouponStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record CouponUsabilityStatus(
    @Schema(description = "사용 가능 상태", example = "USABLE")
    CouponStatus status,

    @Schema(description = "상태 설명 메시지", example = "사용 가능한 쿠폰입니다.")
    String reason
) {

}