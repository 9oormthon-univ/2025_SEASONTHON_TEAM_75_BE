package com.trashheroesbe.feature.coupon.api;

import com.trashheroesbe.feature.coupon.dto.request.CouponCreateRequest;
import com.trashheroesbe.feature.coupon.dto.response.CouponCreateResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponQrResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.trashheroesbe.global.auth.security.CustomerDetails;

@Tag(name = "Coupon", description = "쿠폰 생성/조회 API")
public interface CouponPartnerControllerApi {

    @Operation(summary = "쿠폰 생성", description = "파트너가 쿠폰을 생성합니다.")
    ApiResponse<CouponCreateResponse> createCoupon(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @Valid @RequestBody CouponCreateRequest request
    );

    @Operation(summary = "QR로 쿠폰 조회", description = "쿠폰 ID와 QR 토큰으로 쿠폰 정보를 조회합니다.")
    ApiResponse<CouponQrResponse> getCouponByQr(
        @Parameter(description = "쿠폰 ID", required = true) @RequestParam Long couponId,
        @Parameter(description = "QR 토큰", required = true) @RequestParam String qrToken
    );
}
