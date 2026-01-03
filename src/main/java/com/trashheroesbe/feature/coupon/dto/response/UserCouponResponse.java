package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import com.trashheroesbe.feature.coupon.domain.type.CouponStatus;
import com.trashheroesbe.feature.coupon.domain.type.CouponType;
import java.time.LocalDateTime;

public record UserCouponResponse(
    Long userCouponId,
    CouponStatus couponStatus,
    LocalDateTime purchasedAt,
    LocalDateTime updateAt,
    Long couponId,
    String couponTitle,
    String couponContent,
    CouponType couponType,
    String qrImageUrl,
    String partnerName,
    String partnerImageUrl,
    LocalDateTime usedAt
) {

    public static UserCouponResponse from(UserCoupon userCoupon) {
        return new UserCouponResponse(
            userCoupon.getId(),
            userCoupon.getStatus(),
            userCoupon.getCreatedAt(),
            userCoupon.getUpdatedAt(),
            userCoupon.getCoupon().getId(),
            userCoupon.getCoupon().getTitle(),
            userCoupon.getCoupon().getContent(),
            userCoupon.getCoupon().getType(),
            userCoupon.getQrImageUrl(),
            userCoupon.getCoupon().getPartner().getPartnerName(),
            userCoupon.getCoupon().getPartner().getImageUrl(),
            userCoupon.getUsedAt()
        );
    }
}
