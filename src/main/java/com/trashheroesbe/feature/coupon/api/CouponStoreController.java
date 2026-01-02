package com.trashheroesbe.feature.coupon.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.coupon.application.CouponStoreService;
import com.trashheroesbe.feature.coupon.dto.request.CouponPurchaseRequest;
import com.trashheroesbe.feature.coupon.dto.response.CouponStoreListResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponStoreResponse;
import com.trashheroesbe.feature.coupon.dto.response.PurchaseUserCouponResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/coupons")
public class CouponStoreController implements CouponStoreControllerApi {

    private final CouponStoreService couponStoreService;

    @Override
    @GetMapping()
    public ApiResponse<List<CouponStoreListResponse>> getCouponStoreList() {
        List<CouponStoreListResponse> responses = couponStoreService.getCouponStoreList();
        return ApiResponse.success(OK, responses);
    }

    @Override
    @GetMapping("/{couponId}")
    public ApiResponse<CouponStoreResponse> getCouponStoreById(@PathVariable Long couponId) {
        CouponStoreResponse response = couponStoreService.getCouponStoreById(couponId);
        return ApiResponse.success(OK, response);
    }

    @Override
    @PostMapping("/purchase")
    public ApiResponse<PurchaseUserCouponResponse> purchaseCoupon(
        @RequestBody @Valid CouponPurchaseRequest request,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        PurchaseUserCouponResponse response = couponStoreService.purchaseCoupon(
            request,
            customerDetails.getUser()
        );
        return ApiResponse.success(OK, response);
    }
}
