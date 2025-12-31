package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import com.trashheroesbe.feature.partner.dto.response.PartnerStoreResponse;
import java.time.LocalDateTime;

public record CouponStoreResponse(
    Long couponId,
    String title,
    String content,
    String couponType,
    Integer pointCost,
    LocalDateTime crateAt,
    LocalDateTime updateAt,
    PartnerStoreResponse partnerResponse
) {

    public static CouponStoreResponse from(Coupon coupon) {
        return new CouponStoreResponse(
            coupon.getId(),
            coupon.getTitle(),
            coupon.getTitle(),
            coupon.getType().getDescription(),
            coupon.getPointCost(),
            coupon.getCreatedAt(),
            coupon.getUpdatedAt(),
            PartnerStoreResponse.from(coupon.getPartner())
        );
    }
}
