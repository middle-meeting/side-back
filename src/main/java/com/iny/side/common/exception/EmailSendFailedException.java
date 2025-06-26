package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 이메일 전송에 실패했을 때 발생하는 예외
 */
public class EmailSendFailedException extends BusinessException {

    public EmailSendFailedException(String email, Throwable cause) {
        super("error.email.send_failed", HttpStatus.INTERNAL_SERVER_ERROR, email);
        initCause(cause);
    }

    public EmailSendFailedException(String email) {
        super("error.email.send_failed", HttpStatus.INTERNAL_SERVER_ERROR, email);
    }

    public EmailSendFailedException() {
        super("error.email.send_failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
