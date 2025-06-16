package com.iny.side.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String messageCode;
    private final Object[] args;

    public BusinessException(String messageCode, HttpStatus httpStatus, Object... args) {
        super(messageCode);
        this.httpStatus = httpStatus;
        this.messageCode = messageCode;
        this.args = args;
    }
}