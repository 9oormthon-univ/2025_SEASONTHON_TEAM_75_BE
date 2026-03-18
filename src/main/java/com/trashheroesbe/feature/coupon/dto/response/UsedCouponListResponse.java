package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import java.time.LocalDateTime;

public record UsedCouponListResponse(
    Long UserCouponId,
    String CouponStatusDescription,
    LocalDateTime usedAt,

    Long userId,
    String userNickName,

    Long couponId,
    String couponTitle,
    String couponTypeDescription
) {

    public static UsedCouponListResponse from(UserCoupon userCoupon) {
        return new UsedCouponListResponse(
            userCoupon.getId(),
            userCoupon.getStatus().getDescription(),
            userCoupon.getUsedAt(),
            userCoupon.getUser().getId(),
            userCoupon.getUser().getNickname(),
            userCoupon.getCoupon().getId(),
            userCoupon.getCoupon().getTitle(),
            userCoupon.getCoupon().getType().getDescription()
        );
    }
}
