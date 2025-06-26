package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 잘못된 인증번호이거나 만료된 인증번호일 때 발생하는 예외
 */
public class InvalidVerificationCodeException extends BusinessException {

    public InvalidVerificationCodeException() {
        super("error.verification_code.invalid", HttpStatus.BAD_REQUEST);
    }

    public InvalidVerificationCodeException(String messageKey) {
        super(messageKey, HttpStatus.BAD_REQUEST);
    }
}
