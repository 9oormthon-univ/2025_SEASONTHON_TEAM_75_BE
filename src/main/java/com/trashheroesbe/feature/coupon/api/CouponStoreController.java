package com.trashheroesbe.feature.coupon.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.coupon.application.CouponStoreService;
import com.trashheroesbe.feature.coupon.dto.response.CouponStoreListResponse;
import com.trashheroesbe.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
        List<CouponStoreListResponse> responses = couponStoreService.getCouponStroeList();
        return ApiResponse.success(OK, responses);
    }
}
