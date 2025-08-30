package com.trashheroesbe.global.response;

import com.trashheroesbe.global.response.type.ErrorCode;
import com.trashheroesbe.global.response.type.SuccessCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "응답 객체")
public record ApiResponse<T>(
    @Schema(description = "상태 코드", example = "200")
    int httpCode,
    @Schema(description = "상태 이름", example = "OK")
    String httpStatus,
    @Schema(description = "응답 내용", example = "요청에 성공하였습니다.")
    String message,
    @Schema(description = "응답 데이터")
    T data
) {

    public static <T> ApiResponse<T> success(final SuccessCode successCode, final T data) {
        return ApiResponse.<T>builder()
            .httpCode(successCode.getStatus().value())
            .httpStatus(successCode.getStatus().name())
            .message(successCode.getMessage())
            .data(data)
            .build();
    }

    public static ApiResponse<Void> success(final SuccessCode successCode) {
        return success(successCode, null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message, T data) {
        return ApiResponse.<T>builder()
            .httpCode(errorCode.getStatus().value())
            .httpStatus(errorCode.getStatus().name())
            .message(message)
            .data(data)
            .build();
    }

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return error(errorCode, errorCode.getMessage(), null);
    }

    public static ApiResponse<Void> error(ErrorCode errorCode, String message) {
        return error(errorCode, message, null);
    }
}
