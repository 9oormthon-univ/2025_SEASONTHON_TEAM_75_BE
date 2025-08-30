package com.trashheroesbe.global.response.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    OK(HttpStatus.OK, "요청에 성공하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
