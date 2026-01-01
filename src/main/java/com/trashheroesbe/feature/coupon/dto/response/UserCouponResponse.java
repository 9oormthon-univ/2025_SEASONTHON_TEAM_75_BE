package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;

public record UserCouponResponse(

) {

    public static UserCouponResponse from(UserCoupon userCoupon) {
        return new UserCouponResponse(

        );
    }
}
