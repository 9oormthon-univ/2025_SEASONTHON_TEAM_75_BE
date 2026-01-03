package com.trashheroesbe.feature.coupon.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.coupon.application.CouponService;
import com.trashheroesbe.feature.coupon.dto.request.CouponCreateRequest;
import com.trashheroesbe.feature.coupon.dto.request.CouponUpdateRequest;
import com.trashheroesbe.feature.coupon.dto.response.CouponCreateResponse;
import com.trashheroesbe.feature.coupon.dto.response.PartnerCouponResponse;
import com.trashheroesbe.global.response.ApiResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/partner")
public class CouponPartnerController implements CouponPartnerControllerApi {

    private final CouponService couponService;

    @Override
    @GetMapping("/coupons")
    public ApiResponse<List<PartnerCouponResponse>> getPartnerCoupons(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        return ApiResponse.success(OK, couponService.getPartnerCoupons(customerDetails));
    }

    @Override
    @PostMapping("/coupons")
    public ApiResponse<CouponCreateResponse> createCoupon(
            @AuthenticationPrincipal CustomerDetails customerDetails,
            @Valid @RequestBody CouponCreateRequest request
    ) {
        return ApiResponse.success(OK, couponService.createCoupon(customerDetails, request));
    }

    @Override
    @PatchMapping("/coupons/{couponId}")
    public ApiResponse<CouponCreateResponse> updateCoupon(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable Long couponId,
        @Valid @RequestBody CouponUpdateRequest request
    ) {
        return ApiResponse.success(OK, couponService.updateCoupon(customerDetails, couponId, request));
    }

    @Override
    @DeleteMapping("/coupons/{couponId}")
    public ApiResponse<Void> deleteCoupon(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable Long couponId
    ) {
        couponService.deleteCoupon(customerDetails, couponId);
        return ApiResponse.success(OK);
    }

}
