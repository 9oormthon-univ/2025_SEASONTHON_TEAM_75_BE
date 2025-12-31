package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import com.trashheroesbe.feature.partner.dto.response.PartnerStoreResponse;
import java.time.LocalDateTime;

public record CouponStoreListResponse(
    Long couponId,
    String title,
    String couponType,
    Integer pointCost,
    LocalDateTime createAt,
    LocalDateTime updateAt,
    PartnerStoreResponse partnerResponse
) {

    public static CouponStoreListResponse from(Coupon coupon) {
        return new CouponStoreListResponse(
            coupon.getId(),
            coupon.getTitle(),
            coupon.getType().getDescription(),
            coupon.getPointCost(),
            coupon.getCreatedAt(),
            coupon.getUpdatedAt(),
            PartnerStoreResponse.from(coupon.getPartner())
        );
    }
}
