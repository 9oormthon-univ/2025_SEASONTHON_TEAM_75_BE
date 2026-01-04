package com.trashheroesbe.feature.coupon.api;

import com.trashheroesbe.feature.coupon.dto.request.CouponCreateRequest;
import com.trashheroesbe.feature.coupon.dto.request.CouponUpdateRequest;
import com.trashheroesbe.feature.coupon.dto.response.CouponCreateResponse;
import com.trashheroesbe.feature.coupon.dto.response.PartnerCouponResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import com.trashheroesbe.global.auth.security.CustomerDetails;

@Tag(name = "Coupon", description = "쿠폰 생성/조회 API")
public interface CouponPartnerControllerApi {

    @Operation(summary = "파트너 쿠폰 전체 조회", description = "파트너가 발급한 쿠폰 목록을 조회합니다.")
    ApiResponse<List<PartnerCouponResponse>> getPartnerCoupons(
        @AuthenticationPrincipal CustomerDetails customerDetails
    );

    @Operation(summary = "쿠폰 생성", description = "파트너가 쿠폰을 생성합니다.")
    ApiResponse<CouponCreateResponse> createCoupon(
        @AuthenticationPrincipal CustomerDetails customerDetails,
            @Valid @RequestBody CouponCreateRequest request
    );

    @Operation(summary = "쿠폰 수정", description = "파트너가 본인 소유의 쿠폰을 수정합니다.")
    ApiResponse<CouponCreateResponse> updateCoupon(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @Parameter(description = "쿠폰 ID", required = true) @PathVariable Long couponId,
        @Valid @RequestBody CouponUpdateRequest request
    );

    @Operation(summary = "쿠폰 삭제", description = "파트너가 본인 소유의 쿠폰을 삭제합니다.")
    ApiResponse<Void> deleteCoupon(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @Parameter(description = "쿠폰 ID", required = true) @PathVariable Long couponId
    );

}
