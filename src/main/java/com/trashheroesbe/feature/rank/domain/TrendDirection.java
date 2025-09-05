package com.trashheroesbe.feature.rank.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrendDirection {
    UP("상승", "순위가 올랐습니다"),
    DOWN("하락", "순위가 내려갔습니다"),
    SAME("유지", "순위가 동일합니다");

    private final String description;
    private final String message;

}
