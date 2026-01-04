package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;

public record CouponCreateResponse(
        Long couponId,
        Long partnerId,
        String title,
        String content
) {
    public static CouponCreateResponse from(Coupon coupon) {
        return new CouponCreateResponse(
                coupon.getId(),
                coupon.getPartner().getId(),
                coupon.getTitle(),
                coupon.getContent()
        );
    }
}
