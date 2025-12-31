package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import java.time.LocalDateTime;
import java.util.List;

public record CouponStoreListResponse(
    Long couponId,
    String title,
    String couponType,
    Integer pointCost,
    LocalDateTime createAt,
    LocalDateTime updateAt
) {

    public static CouponStoreListResponse from(Coupon coupon) {
        return new CouponStoreListResponse(
            coupon.getId(),
            coupon.getTitle(),
            coupon.getType().getDescription(),
            coupon.getPointCost(),
            coupon.getCreatedAt(),
            coupon.getUpdatedAt()
        );
    }
}
