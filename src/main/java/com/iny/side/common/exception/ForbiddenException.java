package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(String messageKey, Object... args) {
        super(messageKey, HttpStatus.FORBIDDEN, args);
    }
}
