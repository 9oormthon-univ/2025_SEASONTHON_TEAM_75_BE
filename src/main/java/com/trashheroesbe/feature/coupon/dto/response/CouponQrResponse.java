package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import com.trashheroesbe.feature.coupon.domain.type.CouponType;
import com.trashheroesbe.feature.coupon.domain.type.DiscountType;


public record CouponQrResponse(
    Long couponId,
    Long partnerId,
    String title,
    String content,
    CouponType type,
    Integer pointCost,
    DiscountType discountType,
    Integer discountValue,
    String qrImageUrl
) {

    public static CouponQrResponse from(Coupon coupon) {
        return new CouponQrResponse(
            coupon.getId(),
            coupon.getPartnerId(),
            coupon.getTitle(),
            coupon.getContent(),
            coupon.getType(),
            coupon.getPointCost(),
            coupon.getDiscountType(),
            coupon.getDiscountValue(),
            coupon.getQrImageUrl()
        );
    }
}

