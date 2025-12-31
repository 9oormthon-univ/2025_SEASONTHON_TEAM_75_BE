package com.trashheroesbe.feature.coupon.application;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import com.trashheroesbe.feature.coupon.dto.response.CouponStoreListResponse;
import com.trashheroesbe.feature.coupon.infrastructure.CouponRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponStoreService {

    private final CouponRepository couponRepository;

    public List<CouponStoreListResponse> getCouponStroeList() {
        List<Coupon> coupons = couponRepository.findAll();
        return coupons.stream()
            .map(CouponStoreListResponse::from)
            .collect(Collectors.toList());
    }
}
