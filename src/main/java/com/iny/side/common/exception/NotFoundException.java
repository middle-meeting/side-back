package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {
    public NotFoundException(String targetName, String id) {
        super("error.not_found", HttpStatus.NOT_FOUND, targetName, id);
    }

    public NotFoundException(String targetName, Long id) {
        this(targetName, String.valueOf(id));
    }
}
