package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message) {
        super("error.forbidden", HttpStatus.FORBIDDEN, message);
    }
}
