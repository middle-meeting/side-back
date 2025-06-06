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

}
