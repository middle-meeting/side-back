package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {
    public NotFoundException(String targetName, String id) {
        super(targetName + "의 ID " + id + "이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String targetName, Long id) {
        super(targetName + "의 ID " + id + "이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
