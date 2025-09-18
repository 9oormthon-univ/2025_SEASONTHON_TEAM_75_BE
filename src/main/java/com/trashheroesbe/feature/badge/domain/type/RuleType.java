package com.trashheroesbe.feature.badge.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RuleType {
    TOTAL_COUNT("전체 분석 누적 수"),
    TYPE_SET("서로 다른 타입 수"),
    TYPE_COUNT("특정 타입 누적 수"),
    STREAK("연속 일수");

    private final String description;
}
