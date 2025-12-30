package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;

public record CouponCreateResponse(
        Long couponId,
        Long partnerId,
        String title,
        String content,
        String qrImageUrl
) {
    public static CouponCreateResponse from(Coupon coupon) {
        return new CouponCreateResponse(
                coupon.getId(),
                coupon.getPartnerId(),
                coupon.getTitle(),
                coupon.getContent(),
                coupon.getQrImageUrl()
        );
    }
}
