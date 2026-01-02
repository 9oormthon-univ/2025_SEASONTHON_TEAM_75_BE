package com.trashheroesbe.feature.coupon.dto.request;

import jakarta.validation.constraints.NotNull;

public record CouponPurchaseRequest(
    @NotNull(message = "쿠폰 ID는 필수입니다.")
    Long couponId
) {

}
