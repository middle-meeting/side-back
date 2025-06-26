package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 이메일 인증이 완료되지 않았을 때 발생하는 예외
 */
public class EmailNotVerifiedException extends BusinessException {
    
    public EmailNotVerifiedException(String email) {
        super("error.email.not_verified", HttpStatus.BAD_REQUEST, email);
    }
    
    public EmailNotVerifiedException() {
        super("error.email.not_verified", HttpStatus.BAD_REQUEST);
    }
}
