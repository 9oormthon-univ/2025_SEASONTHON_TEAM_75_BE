package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import com.trashheroesbe.feature.coupon.domain.type.CouponType;
import com.trashheroesbe.feature.coupon.domain.type.DiscountType;
import java.time.LocalDateTime;

public record PartnerCouponResponse(
    Long couponId,
    String title,
    String content,
    CouponType type,
    Integer pointCost,
    DiscountType discountType,
    Integer discountValue,
    Integer totalStock,
    Integer issuedCount,
    Boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static PartnerCouponResponse from(Coupon coupon) {
        return new PartnerCouponResponse(
            coupon.getId(),
            coupon.getTitle(),
            coupon.getContent(),
            coupon.getType(),
            coupon.getPointCost(),
            coupon.getDiscountType(),
            coupon.getDiscountValue(),
            coupon.getTotalStock(),
            coupon.getIssuedCount(),
            coupon.getIsActive(),
            coupon.getCreatedAt(),
            coupon.getUpdatedAt()
        );
    }
}

