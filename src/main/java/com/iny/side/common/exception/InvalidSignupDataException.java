package com.iny.side.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 회원가입 데이터가 유효하지 않을 때 발생하는 예외
 * (역할별 필수 필드 누락 등)
 */
public class InvalidSignupDataException extends BusinessException {

    public InvalidSignupDataException(String messageKey) {
        super(messageKey, HttpStatus.BAD_REQUEST);
    }

    public static InvalidSignupDataException missingGrade() {
        return new InvalidSignupDataException("error.signup.missing_grade");
    }

    public static InvalidSignupDataException missingStudentId() {
        return new InvalidSignupDataException("error.signup.missing_student_id");
    }

    public static InvalidSignupDataException missingEmployeeId() {
        return new InvalidSignupDataException("error.signup.missing_employee_id");
    }
}
