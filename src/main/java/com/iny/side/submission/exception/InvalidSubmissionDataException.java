package com.iny.side.submission.exception;

import com.iny.side.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidSubmissionDataException extends BusinessException {

    public InvalidSubmissionDataException(String field) {
        super("submission.invalid_data", HttpStatus.BAD_REQUEST, field + "은(는) 필수 입력값입니다.");
    }

    public InvalidSubmissionDataException(String code, String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
