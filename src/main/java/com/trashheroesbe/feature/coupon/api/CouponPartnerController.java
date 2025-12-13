package com.trashheroesbe.feature.coupon.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.coupon.application.CouponService;
import com.trashheroesbe.feature.coupon.dto.request.CouponCreateRequest;
import com.trashheroesbe.feature.coupon.dto.response.CouponCreateResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponQrResponse;
import com.trashheroesbe.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CouponPartnerController implements CouponPartnerControllerApi {

    private final CouponService couponService;

    @Override
    @PostMapping("/partners/coupons")
    public ApiResponse<CouponCreateResponse> createCoupon(
            @Valid @RequestBody CouponCreateRequest request
    ) {
        return ApiResponse.success(OK, couponService.createCoupon(request));
    }

    @Override
    @GetMapping("/coupons/qr")
    public ApiResponse<CouponQrResponse> getCouponByQr(
        @RequestParam Long couponId,
        @RequestParam String qrToken
    ) {
        return ApiResponse.success(OK, couponService.findByQr(couponId, qrToken));
    }
}
