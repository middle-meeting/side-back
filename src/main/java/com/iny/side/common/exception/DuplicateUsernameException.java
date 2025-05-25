package com.iny.side.common.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super(username + " already exists");
    }
}
