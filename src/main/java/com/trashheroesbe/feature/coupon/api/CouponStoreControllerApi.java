package com.trashheroesbe.feature.coupon.api;


import com.trashheroesbe.feature.coupon.dto.response.CouponStoreListResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponStoreResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Store", description = "상점 관련 API")
public interface CouponStoreControllerApi {

    @Operation(summary = "상점 아이템 전체조회", description = "상점의 아이템을 전체조회 합니다.")
    ApiResponse<List<CouponStoreListResponse>> getCouponStoreList();

    @Operation(summary = "상점 아이템 상세조회", description = "상점의 아이템을 상세조회 합니다.")
    ApiResponse<CouponStoreResponse> getCouponStoreById(Long couponId);

}
