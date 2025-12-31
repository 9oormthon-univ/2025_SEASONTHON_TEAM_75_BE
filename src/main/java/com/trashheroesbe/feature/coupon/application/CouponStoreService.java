package com.trashheroesbe.feature.coupon.application;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import com.trashheroesbe.feature.coupon.dto.response.CouponStoreListResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponStoreResponse;
import com.trashheroesbe.feature.coupon.infrastructure.CouponRepository;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.response.type.ErrorCode;
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

    public CouponStoreResponse getCouponStoreById(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
        return CouponStoreResponse.from(coupon);
    }
}
