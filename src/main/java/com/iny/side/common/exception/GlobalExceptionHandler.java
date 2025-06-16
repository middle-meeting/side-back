package com.iny.side.common.exception;

import com.iny.side.common.response.BasicResponse;
import com.iny.side.common.response.ErrorDetail;
import com.iny.side.common.response.ErrorPayload;
import org.springframework.context.MessageSource;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
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
                messageSource.getMessage("error.validation", null, Locale.getDefault()),
                errorPayload
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BasicResponse<?>> handleBusinessException(BusinessException ex) {
        String message = messageSource.getMessage(ex.getMessageCode(), ex.getArgs(), Locale.getDefault());
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code(ex.getMessageCode())
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BasicResponse<?>> handleRuntimeException(RuntimeException ex) {
        String message = messageSource.getMessage("error.internal", null, Locale.getDefault());
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("error.internal")
                .message(message)
                .build();

        ErrorPayload errorPayload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();

        BasicResponse<?> response = BasicResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message,
                errorPayload
        );

        return ResponseEntity.internalServerError().body(response);
    }
}
