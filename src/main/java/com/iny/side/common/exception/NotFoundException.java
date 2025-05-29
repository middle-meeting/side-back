package com.iny.side.common.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String targetName, String id) {
        super(targetName + "의 ID " + id + "이 존재하지 않습니다.");
    }
    public NotFoundException(String targetName, Long id) {
        super(targetName + "의 ID " + id + "이 존재하지 않습니다.");
    }
}
