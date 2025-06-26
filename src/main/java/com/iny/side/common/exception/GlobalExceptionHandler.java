package com.iny.side.common.exception;

import com.iny.side.common.BasicResponse;
import com.iny.side.common.ErrorDetail;
import com.iny.side.common.ErrorPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

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
                getMessage("validation.failed"),
                errorPayload
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BasicResponse<?>> handleBusinessException(BusinessException ex) {
        String message = getMessage(ex.getMessageKey(), ex.getArgs());
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("BUSINESS_ERROR")
                .message(message)
                .build();

        ErrorPayload errorPayload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();

        BasicResponse<?> response = BasicResponse.error(
                ex.getHttpStatus(),
                message,
                errorPayload
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BasicResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("INVALID_ARGUMENT")
                .message(ex.getMessage())
                .build();

        ErrorPayload errorPayload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();

        BasicResponse<?> response = BasicResponse.error(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                errorPayload
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BasicResponse<?>> handleRuntimeException(RuntimeException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message(getMessage("server.unexpected"))
                .build();

        ErrorPayload errorPayload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();

        BasicResponse<?> response = BasicResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getMessage("server.unexpected"),
                errorPayload
        );

        return ResponseEntity.internalServerError().body(response);
    }
}
