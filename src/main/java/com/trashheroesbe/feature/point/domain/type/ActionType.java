package com.trashheroesbe.feature.point.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionType {
    EARNED("적립"),
    USED("사용");

    private final String description;
}
