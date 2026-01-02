package com.trashheroesbe.feature.coupon.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponType {
    ONLINE("온라인"),
    OFFLINE("오프라인");

    private final String description;
}

