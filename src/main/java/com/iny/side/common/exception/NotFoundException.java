package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {
    public NotFoundException(String targetName) {
        super("error.not_found", HttpStatus.NOT_FOUND, targetName);
    }
}
