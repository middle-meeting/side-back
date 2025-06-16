package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {
    public NotFoundException(String targetName) {
        super(targetName + "이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
