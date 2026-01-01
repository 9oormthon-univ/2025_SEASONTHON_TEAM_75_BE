package com.trashheroesbe.feature.coupon.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;


import com.trashheroesbe.feature.coupon.application.UserCouponService;
import com.trashheroesbe.feature.coupon.dto.response.UserCouponListResponse;
import com.trashheroesbe.feature.coupon.dto.response.UserCouponResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/my/coupons")
public class UserCouponController implements UserCouponControllerApi {

    private final UserCouponService userCouponService;

    @Override
    @GetMapping
    public ApiResponse<List<UserCouponListResponse>> getUserCouponList(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<UserCouponListResponse> responses = userCouponService.getUserCouponList(
            customerDetails.getUser());
        return ApiResponse.success(OK, responses);
    }

    @Override
    @GetMapping("/{userCouponId}")
    public ApiResponse<UserCouponResponse> getUserCouponById(
        @PathVariable Long userCouponId,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        UserCouponResponse response = userCouponService.getUserCouponById(
            userCouponId,
            customerDetails.getUser()
        );
        return ApiResponse.success(OK, null);
    }
}