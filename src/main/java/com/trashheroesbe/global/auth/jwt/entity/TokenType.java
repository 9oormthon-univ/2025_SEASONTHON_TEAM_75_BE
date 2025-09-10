package com.trashheroesbe.global.auth.jwt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    GUEST_ACCESS_TOKEN("access_token", 3_600_000L),
    ACCESS_TOKEN("access_token", 10_800_000L), // 3시간
    REFRESH_TOKEN("refresh_token", 1_209_600_000L); // 2주

    private final String name;
    private final Long validTime;
}
