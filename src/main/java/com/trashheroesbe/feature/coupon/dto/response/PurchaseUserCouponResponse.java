package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import java.time.LocalDateTime;

public record PurchaseUserCouponResponse(
    Long userCouponId,
    Long couponId,
    String couponTitle,
    Integer pointsUsed,
    LocalDateTime purchasedAt
) {

    public static PurchaseUserCouponResponse from(UserCoupon userCoupon) {
        return new PurchaseUserCouponResponse(
            userCoupon.getId(),
            userCoupon.getCoupon().getId(),
            userCoupon.getCoupon().getTitle(),
            userCoupon.getCoupon().getPointCost(),
            userCoupon.getCreatedAt()
        );
    }
}
