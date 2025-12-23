package com.trashheroesbe.feature.point.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointReason {
    TRASH_CREATED("쓰레기 등록"),
    BADGE_EARNED("배지 획득"),
    COUPON_PURCHASE("쿠폰 구매"),
    ADMIN_GRANT("관리자 지급"),
    ADMIN_DEDUCT("관리자 차감");

    private final String description;
}
