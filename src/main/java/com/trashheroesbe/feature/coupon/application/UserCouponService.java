package com.trashheroesbe.feature.coupon.application;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import com.trashheroesbe.feature.coupon.dto.response.UserCouponListResponse;
import com.trashheroesbe.feature.coupon.dto.response.UserCouponResponse;
import com.trashheroesbe.feature.coupon.infrastructure.UserCouponRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.response.type.ErrorCode;
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

    public UserCouponResponse getUserCouponById(Long userCouponId, User user) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        if (!userCoupon.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED_EXCEPTION);
        }

        return UserCouponResponse.from(userCoupon);
    }
}
