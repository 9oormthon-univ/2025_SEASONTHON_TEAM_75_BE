package com.trashheroesbe.global.exception;


import com.trashheroesbe.global.response.ApiResponse;
import com.trashheroesbe.global.response.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(final Exception e) {
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error(errorCode.getMessage(), e);
        return ResponseEntity.status(errorCode.getStatus())
            .body(ApiResponse.error(errorCode));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(final BusinessException e) {
        log.warn(e.getMessage(), e);
        final HttpStatus httpStatus = e.getErrorCode().getStatus();
        return ResponseEntity.status(httpStatus)
            .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException e
    ) {
        String errorMessage = e.getBindingResult()
            .getAllErrors()
            .getFirst()
            .getDefaultMessage();

        final ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        return ResponseEntity.status(errorCode.getStatus())
            .body(ApiResponse.error(errorCode, errorMessage));
    }
}
