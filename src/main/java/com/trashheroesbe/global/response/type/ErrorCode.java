package com.trashheroesbe.global.response.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "입력값 검증에 실패했습니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "요청한 정보로 엔터티를 찾을 수 없습니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),

    // auth
    NOT_EXISTS_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다."),
    NOT_FOUND_ATTRIBUTES(HttpStatus.NOT_FOUND, "요청 attributes(uri)를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "일치하지 않은 비밀번호입니다."),

    // s3
    S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 업로드에 실패했습니다."),
    S3_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"S3 파일 삭제에 실패했습니다."),

    // user
    DUPLICATE_USER_DISTRICT(HttpStatus.BAD_REQUEST, "중복된 자치구 입니다."),
    MAX_USER_DISTRICTS_EXCEEDED(HttpStatus.BAD_REQUEST, "자치구는 최대 2개만 가질 수 있습니다."),
    NOT_FOUND_USER_DISTRICTS(HttpStatus.NOT_FOUND, "자치구 등록을 하지 않으셨습니다. 등록해주세요."),
    NOT_FOUND_DEFAULT_USER_DISTRICTS(HttpStatus.NOT_FOUND, "대표 유저 자치구를 찾을 수 없습니다."),

    //partner
    EXISTS_EMAIL(HttpStatus.NOT_FOUND, "이미 사용중인 이메일 입니다."),

    // district
    UNSUPPORTED_DISTRICT(HttpStatus.BAD_REQUEST, "서비스에서 지원하지 않는 지역입니다."),

    // trash
    NOT_EXISTS_TRASH_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "쓰레기 타입이 존재하지 않습니다. 개발자에게 문의 주세요."),
    NOT_EXISTS_TRASH_ITEM(HttpStatus.INTERNAL_SERVER_ERROR, "쓰레기 품목이 존재하지 않습니다. 개발자에게 문의 주세요."),
    NOT_EXISTS_TRASH_DESCRIPTION(HttpStatus.INTERNAL_SERVER_ERROR, "쓰레기 설명이 존재하지 않습니다. 개발자에게 문의 주세요."),
    INVALID_SEARCH_KEYWORD(HttpStatus.BAD_REQUEST, "검색 키워드가 비어있습니다."),

    // gpt
    ERROR_GPT_CALL(HttpStatus.BAD_GATEWAY, "GPT 호출에 실패했습니다."),
    EMPTY_GPT_RESPONSE(HttpStatus.BAD_GATEWAY, "GPT 응답이 비었습니다."),
    FAIL_PARSING_RESPONSE(HttpStatus.UNPROCESSABLE_ENTITY, "응답 파싱에 실패했습니다"),

    // qr
    QR_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "QR 코드 생성에 실패했습니다."),

    // point
    POINT_AMOUNT_MUST_BE_POSITIVE(HttpStatus.BAD_REQUEST, "적립 포인트는 0보다 커야 합니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
