package com.trashheroesbe.feature.search.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogSource {

    QUESTION("챗봇"),
    IMAGE("쓰레기 분석");

    private final String description;
}
