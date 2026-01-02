package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import com.trashheroesbe.feature.coupon.domain.type.CouponStatus;
import com.trashheroesbe.feature.coupon.domain.type.CouponType;
import java.time.LocalDateTime;

public record UserCouponListResponse(
    Long userCouponId,
    CouponStatus couponStatus,
    LocalDateTime purchasedAt,
    LocalDateTime updateAt,
    Long couponId,
    String couponTitle,
    CouponType couponType,
    String partnerName,
    String partnerImageUrl,
    LocalDateTime usedAt
) {

    public static UserCouponListResponse from(UserCoupon userCoupon) {
        return new UserCouponListResponse(
            userCoupon.getId(),
            userCoupon.getStatus(),
            userCoupon.getCreatedAt(),
            userCoupon.getUpdatedAt(),
            userCoupon.getCoupon().getId(),
            userCoupon.getCoupon().getTitle(),
            userCoupon.getCoupon().getType(),
            userCoupon.getCoupon().getPartner().getPartnerName(),
            userCoupon.getCoupon().getPartner().getImageUrl(),
            userCoupon.getUsedAt()
        );
    }
}
