package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

public class EmailResendTooSoonException extends BusinessException {

    public EmailResendTooSoonException(long remainingSeconds) {
        super("error.email.resend_too_soon", HttpStatus.TOO_MANY_REQUESTS, remainingSeconds);
    }
}
