package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateUsernameException extends BusinessException {
    public DuplicateUsernameException(String username) {
        super("error.username.duplicate", HttpStatus.BAD_REQUEST, username);
    }
}
