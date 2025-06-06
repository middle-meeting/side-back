package com.iny.side.common.exception;

import com.iny.side.common.BasicResponse;
import com.iny.side.common.ErrorDetail;
import com.iny.side.common.ErrorPayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BasicResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errorDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorDetail.builder()
                        .code("VALIDATION_ERROR")
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();

        ErrorPayload errorPayload = ErrorPayload.builder()
                .errors(errorDetails)
                .build();

        BasicResponse<?> response = BasicResponse.error(
                HttpStatus.BAD_REQUEST,
                "입력값 검증 실패",
                errorPayload
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BasicResponse<?>> handleBusinessException(BusinessException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("BUSINESS_ERROR")
                .message(ex.getMessage())
                .build();

        ErrorPayload errorPayload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();

        BasicResponse<?> response = BasicResponse.error(
                ex.getHttpStatus(),
                ex.getMessage(),
                errorPayload
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BasicResponse<?>> handleRuntimeException(RuntimeException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("예상치 못한 서버 오류가 발생했습니다.")
                .build();

        ErrorPayload errorPayload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();

        BasicResponse<?> response = BasicResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 내부 오류",
                errorPayload
        );

        return ResponseEntity.internalServerError().body(response);
    }
}
