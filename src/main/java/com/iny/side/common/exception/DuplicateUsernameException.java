package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateUsernameException extends BusinessException {
    public DuplicateUsernameException(String username) {
        super(username + " already exists", HttpStatus.BAD_REQUEST);
    }
}
