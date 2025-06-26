package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignupDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_학생_회원가입_정보는_검증을_통과한다() {
        // given
        SignupDto signupDto = new SignupDto(
                "student@test.com",
                "password123",
                "김학생",
                Role.STUDENT,
                "테스트대학교",
                "컴퓨터공학과",
                3,
                "20211234",
                null,
                "123456"
        );

        // when
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(signupDto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void 유효한_교수_회원가입_정보는_검증을_통과한다() {
        // given
        SignupDto signupDto = new SignupDto(
                "professor@test.com",
                "password123",
                "김교수",
                Role.PROFESSOR,
                "테스트대학교",
                "컴퓨터공학과",
                null,
                null,
                "P001",
                "123456"
        );

        // when
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(signupDto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void 학생_회원가입시_학년이_없으면_검증에_실패한다() {
        // given
        SignupDto signupDto = new SignupDto(
                "student@test.com",
                "password123",
                "김학생",
                Role.STUDENT,
                "테스트대학교",
                "컴퓨터공학과",
                null, // 학년 없음
                "20211234",
                null,
                "123456"
        );

        // when
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(signupDto);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<SignupDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("학년이 입력되지 않았습니다.");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("grade");
    }

    @Test
    void 학생_회원가입시_학번이_없으면_검증에_실패한다() {
        // given
        SignupDto signupDto = new SignupDto(
                "student@test.com",
                "password123",
                "김학생",
                Role.STUDENT,
                "테스트대학교",
                "컴퓨터공학과",
                3,
                null, // 학번 없음
                null,
                "123456"
        );

        // when
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(signupDto);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<SignupDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("학번이 입력되지 않았습니다.");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("studentId");
    }

    @Test
    void 학생_회원가입시_학번이_빈_문자열이면_검증에_실패한다() {
        // given
        SignupDto signupDto = new SignupDto(
                "student@test.com",
                "password123",
                "김학생",
                Role.STUDENT,
                "테스트대학교",
                "컴퓨터공학과",
                3,
                "   ", // 빈 문자열
                null,
                "123456"
        );

        // when
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(signupDto);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<SignupDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("학번이 입력되지 않았습니다.");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("studentId");
    }

    @Test
    void 교수_회원가입시_직번이_없으면_검증에_실패한다() {
        // given
        SignupDto signupDto = new SignupDto(
                "professor@test.com",
                "password123",
                "김교수",
                Role.PROFESSOR,
                "테스트대학교",
                "컴퓨터공학과",
                null,
                null,
                null, // 직번 없음
                "123456"
        );

        // when
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(signupDto);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<SignupDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("직번이 입력되지 않았습니다.");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("employeeId");
    }

    @Test
    void 교수_회원가입시_직번이_빈_문자열이면_검증에_실패한다() {
        // given
        SignupDto signupDto = new SignupDto(
                "professor@test.com",
                "password123",
                "김교수",
                Role.PROFESSOR,
                "테스트대학교",
                "컴퓨터공학과",
                null,
                null,
                "   ", // 빈 문자열
                "123456"
        );

        // when
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(signupDto);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<SignupDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("직번이 입력되지 않았습니다.");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("employeeId");
    }

    @Test
    void 기본_필드_검증도_함께_동작한다() {
        // given
        SignupDto signupDto = new SignupDto(
                "", // 빈 이메일
                "123", // 짧은 비밀번호
                "", // 빈 이름
                Role.STUDENT,
                "", // 빈 학교
                "", // 빈 전공
                null, // 학년 없음
                "20211234",
                null,
                "" // 빈 인증번호
        );

        // when
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(signupDto);

        // then
        assertThat(violations).hasSizeGreaterThan(1); // 여러 검증 오류가 발생해야 함
    }
}
