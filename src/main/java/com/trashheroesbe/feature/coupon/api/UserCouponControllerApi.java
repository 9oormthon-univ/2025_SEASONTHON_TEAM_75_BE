package com.trashheroesbe.feature.coupon.api;

import com.trashheroesbe.feature.coupon.dto.response.UserCouponListResponse;
import com.trashheroesbe.feature.coupon.dto.response.UserCouponResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "UserCoupon", description = "내가 구매한 쿠폰 관련 API")
public interface UserCouponControllerApi {

    @Operation(summary = "내가 구매한 쿠폰 전체조회", description = "내가 구매한 쿠폰을 전체조회 합니다.")
    ApiResponse<List<UserCouponListResponse>> getUserCouponList(CustomerDetails customerDetails);

    @Operation(summary = "내가 구매한 쿠폰 상세조회", description = "내가 구매한 쿠폰을 상세조회 합니다.")
    ApiResponse<UserCouponResponse> getUserCouponById(
        Long userCouponId,
        CustomerDetails customerDetails
    );

    @Operation(summary = "QR로 유저쿠폰 조회", description = "userCouponId와 qrToken으로 유저쿠폰을 조회합니다.")
    ApiResponse<UserCouponResponse> getUserCouponByQr(
        @RequestParam Long userCouponId,
        @RequestParam String qrToken
    );
}
