package com.iny.side.users.application.service;

import com.iny.side.users.mock.FakeEmailVerificationRepository;
import com.iny.side.users.mock.FakeUserRepository;
import com.iny.side.users.mock.FakeVerificationCodeGenerator;
import com.iny.side.common.exception.DuplicateUsernameException;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.domain.entity.EmailVerification;
import com.iny.side.users.web.dto.SignupDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private UserService userService;
    private FakeUserRepository fakeUserRepository;
    private FakeEmailVerificationRepository fakeEmailVerificationRepository;
    private FakeVerificationCodeGenerator fakeVerificationCodeGenerator;
    private EmailVerificationService emailVerificationService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        fakeUserRepository = new FakeUserRepository();
        fakeEmailVerificationRepository = new FakeEmailVerificationRepository();
        fakeVerificationCodeGenerator = new FakeVerificationCodeGenerator();
        emailVerificationService = new EmailVerificationServiceImpl(
                fakeEmailVerificationRepository,
                fakeVerificationCodeGenerator
        );
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(passwordEncoder, fakeUserRepository, emailVerificationService);
    }

    @Test
    void 학생_회원가입을_할_수_있다() {
        // given
        String email = "student@test.com";
        setupEmailVerification(email);
        
        SignupDto signupDto = new SignupDto(
                email,
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
        Account savedAccount = userService.signup(signupDto);

        // then
        assertThat(savedAccount.getUsername()).isEqualTo(email);
        assertThat(savedAccount.getName()).isEqualTo("김학생");
        assertThat(savedAccount.getRole()).isEqualTo(Role.STUDENT);
        assertThat(savedAccount.getSchool()).isEqualTo("테스트대학교");
        assertThat(savedAccount.getMajor()).isEqualTo("컴퓨터공학과");
        assertThat(savedAccount.getGrade()).isEqualTo(3);
        assertThat(savedAccount.getStudentId()).isEqualTo("20211234");
        assertThat(savedAccount.getEmployeeId()).isNull();
        assertThat(savedAccount.getEmailVerified()).isTrue();
        assertThat(passwordEncoder.matches("password123", savedAccount.getPassword())).isTrue();
    }

    @Test
    void 교수_회원가입을_할_수_있다() {
        // given
        String email = "professor@test.com";
        setupEmailVerification(email);
        
        SignupDto signupDto = new SignupDto(
                email,
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
        Account savedAccount = userService.signup(signupDto);

        // then
        assertThat(savedAccount.getUsername()).isEqualTo(email);
        assertThat(savedAccount.getName()).isEqualTo("김교수");
        assertThat(savedAccount.getRole()).isEqualTo(Role.PROFESSOR);
        assertThat(savedAccount.getSchool()).isEqualTo("테스트대학교");
        assertThat(savedAccount.getMajor()).isEqualTo("컴퓨터공학과");
        assertThat(savedAccount.getGrade()).isNull();
        assertThat(savedAccount.getStudentId()).isNull();
        assertThat(savedAccount.getEmployeeId()).isEqualTo("P001");
        assertThat(savedAccount.getEmailVerified()).isTrue();
    }

    @Test
    void 중복된_이메일로_회원가입하면_예외가_발생한다() {
        // given
        String email = "duplicate@test.com";
        setupEmailVerification(email);
        
        SignupDto firstSignup = new SignupDto(
                email, "password123", "첫번째", Role.STUDENT,
                "테스트대학교", "컴퓨터공학과", 1, "20211111", null, "123456"
        );
        userService.signup(firstSignup);

        SignupDto secondSignup = new SignupDto(
                email, "password456", "두번째", Role.STUDENT,
                "테스트대학교", "컴퓨터공학과", 2, "20212222", null, "123456"
        );

        // when & then
        assertThatThrownBy(() -> userService.signup(secondSignup))
                .isInstanceOf(DuplicateUsernameException.class);
    }

    @Test
    void 이메일_인증이_완료되지_않으면_회원가입할_수_없다() {
        // given
        SignupDto signupDto = new SignupDto(
                "unverified@test.com",
                "password123",
                "미인증",
                Role.STUDENT,
                "테스트대학교",
                "컴퓨터공학과",
                1,
                "20211234",
                null,
                "123456"
        );

        // when & then
        assertThatThrownBy(() -> userService.signup(signupDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 인증이 완료되지 않았습니다.");
    }

    @Test
    void 사용자명_존재_여부를_확인할_수_있다() {
        // given
        String email = "existing@test.com";
        setupEmailVerification(email);
        
        SignupDto signupDto = new SignupDto(
                email, "password123", "기존사용자", Role.STUDENT,
                "테스트대학교", "컴퓨터공학과", 1, "20211234", null, "123456"
        );
        userService.signup(signupDto);

        // when & then
        assertThat(userService.existsByUsername(email)).isTrue();
        assertThat(userService.existsByUsername("nonexistent@test.com")).isFalse();
    }

    private void setupEmailVerification(String email) {
        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode("123456")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(3))
                .build();
        emailVerification.verify();
        fakeEmailVerificationRepository.save(emailVerification);
    }
}
