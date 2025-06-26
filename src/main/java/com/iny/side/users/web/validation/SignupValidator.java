package com.iny.side.users.web.validation;

import com.iny.side.users.domain.Role;
import com.iny.side.users.web.dto.SignupDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SignupValidator implements ConstraintValidator<ValidSignup, SignupDto> {

    @Override
    public void initialize(ValidSignup constraintAnnotation) {
        // 초기화 로직 (필요시)
    }

    @Override
    public boolean isValid(SignupDto signupDto, ConstraintValidatorContext context) {
        if (signupDto == null || signupDto.role() == null) {
            return false;
        }

        boolean isValid = true;
        
        // 역할별 필수 필드 검증
        if (signupDto.role() == Role.STUDENT) {
            if (signupDto.grade() == null) {
                addConstraintViolation(context, "학년이 입력되지 않았습니다.", "grade");
                isValid = false;
            }
            if (isBlankOrNull(signupDto.studentId())) {
                addConstraintViolation(context, "학번이 입력되지 않았습니다.", "studentId");
                isValid = false;
            }
        } else if (signupDto.role() == Role.PROFESSOR) {
            if (isBlankOrNull(signupDto.employeeId())) {
                addConstraintViolation(context, "직번이 입력되지 않았습니다.", "employeeId");
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean isBlankOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String propertyPath) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyPath)
                .addConstraintViolation();
    }
}
