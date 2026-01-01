package com.trashheroesbe.feature.coupon.application;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import com.trashheroesbe.feature.coupon.dto.response.UserCouponListResponse;
import com.trashheroesbe.feature.coupon.infrastructure.UserCouponRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;

    public List<UserCouponListResponse> getUserCouponList(User user) {

        List<UserCoupon> userCouponList = userCouponRepository.findByUserId(user.getId());
        return userCouponList.stream()
            .map(UserCouponListResponse::from)
            .collect(Collectors.toList());
    }
}
