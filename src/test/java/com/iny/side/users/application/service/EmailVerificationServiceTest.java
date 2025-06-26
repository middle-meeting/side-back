package com.iny.side.users.application.service;

import com.iny.side.users.mock.FakeEmailVerificationRepository;
import com.iny.side.users.mock.FakeVerificationCodeGenerator;
import com.iny.side.users.domain.entity.EmailVerification;
import com.iny.side.users.web.dto.EmailVerificationConfirmDto;
import com.iny.side.users.web.dto.EmailVerificationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerificationServiceTest {

    private EmailVerificationService emailVerificationService;
    private FakeEmailVerificationRepository fakeEmailVerificationRepository;
    private FakeVerificationCodeGenerator fakeVerificationCodeGenerator;

    @BeforeEach
    void setUp() {
        fakeEmailVerificationRepository = new FakeEmailVerificationRepository();
        fakeVerificationCodeGenerator = new FakeVerificationCodeGenerator();
        emailVerificationService = new EmailVerificationServiceImpl(
                fakeEmailVerificationRepository,
                fakeVerificationCodeGenerator
        );
    }

    @Test
    void 인증번호를_전송할_수_있다() {
        // given
        String email = "test@example.com";
        String expectedCode = "123456";
        fakeVerificationCodeGenerator.setFixedCode(expectedCode);
        EmailVerificationRequestDto requestDto = new EmailVerificationRequestDto(email);

        // when
        emailVerificationService.sendVerificationCode(requestDto);

        // then
        assertThat(fakeEmailVerificationRepository.findLatestByEmail(email)).isPresent();
        EmailVerification verification = fakeEmailVerificationRepository.findLatestByEmail(email).get();
        assertThat(verification.getEmail()).isEqualTo(email);
        assertThat(verification.getVerificationCode()).isEqualTo(expectedCode);
        assertThat(verification.getVerified()).isFalse();
        assertThat(fakeVerificationCodeGenerator.getCallCount()).isEqualTo(1);
    }

    @Test
    void 올바른_인증번호로_인증할_수_있다() {
        // given
        String email = "test@example.com";
        String verificationCode = "123456";
        LocalDateTime now = LocalDateTime.now();
        
        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .createdAt(now)
                .expiresAt(now.plusMinutes(3))
                .build();
        fakeEmailVerificationRepository.save(emailVerification);

        EmailVerificationConfirmDto confirmDto = new EmailVerificationConfirmDto(email, verificationCode);

        // when
        boolean result = emailVerificationService.verifyCode(confirmDto);

        // then
        assertThat(result).isTrue();
        EmailVerification verification = fakeEmailVerificationRepository
                .findByEmailAndVerificationCode(email, verificationCode).get();
        assertThat(verification.isVerified()).isTrue();
    }

    @Test
    void 잘못된_인증번호로는_인증할_수_없다() {
        // given
        String email = "test@example.com";
        String correctCode = "123456";
        String wrongCode = "654321";
        LocalDateTime now = LocalDateTime.now();
        
        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(correctCode)
                .createdAt(now)
                .expiresAt(now.plusMinutes(3))
                .build();
        fakeEmailVerificationRepository.save(emailVerification);

        EmailVerificationConfirmDto confirmDto = new EmailVerificationConfirmDto(email, wrongCode);

        // when
        boolean result = emailVerificationService.verifyCode(confirmDto);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 만료된_인증번호로는_인증할_수_없다() {
        // given
        String email = "test@example.com";
        String verificationCode = "123456";
        LocalDateTime pastTime = LocalDateTime.now().minusMinutes(10);
        
        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .createdAt(pastTime)
                .expiresAt(pastTime.plusMinutes(3))
                .build();
        fakeEmailVerificationRepository.save(emailVerification);

        EmailVerificationConfirmDto confirmDto = new EmailVerificationConfirmDto(email, verificationCode);

        // when
        boolean result = emailVerificationService.verifyCode(confirmDto);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 이미_인증된_코드는_재인증_시_true를_반환한다() {
        // given
        String email = "test@example.com";
        String verificationCode = "123456";
        LocalDateTime now = LocalDateTime.now();
        
        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .createdAt(now)
                .expiresAt(now.plusMinutes(3))
                .build();
        emailVerification.verify(); // 미리 인증 완료(이미 인증됨)
        fakeEmailVerificationRepository.save(emailVerification);

        EmailVerificationConfirmDto confirmDto = new EmailVerificationConfirmDto(email, verificationCode);

        // when
        boolean result = emailVerificationService.verifyCode(confirmDto);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 인증번호를_재전송할_수_있다() {
        // given
        String email = "test@example.com";
        String firstCode = "111111";
        String secondCode = "222222";
        EmailVerificationRequestDto requestDto = new EmailVerificationRequestDto(email);

        // 첫 번째 전송
        fakeVerificationCodeGenerator.setFixedCode(firstCode);
        emailVerificationService.sendVerificationCode(requestDto);

        // when - 재전송 (다른 코드로 설정)
        fakeVerificationCodeGenerator.setFixedCode(secondCode);
        emailVerificationService.resendVerificationCode(requestDto);

        // then
        EmailVerification latestVerification = fakeEmailVerificationRepository.findLatestByEmail(email).get();
        assertThat(latestVerification.getEmail()).isEqualTo(email);
        assertThat(latestVerification.getVerificationCode()).isEqualTo(secondCode);
        assertThat(fakeVerificationCodeGenerator.getCallCount()).isEqualTo(2);
    }

    @Test
    void 이메일_인증_상태를_확인할_수_있다() {
        // given
        String email = "test@example.com";
        String verificationCode = "123456";
        LocalDateTime now = LocalDateTime.now();
        
        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .createdAt(now)
                .expiresAt(now.plusMinutes(5))
                .build();
        emailVerification.verify();
        fakeEmailVerificationRepository.save(emailVerification);

        // when & then
        assertThat(emailVerificationService.isEmailVerified(email)).isTrue();
    }

    @Test
    void 인증되지_않은_이메일은_false를_반환한다() {
        // given
        String email = "test@example.com";

        // when & then
        assertThat(emailVerificationService.isEmailVerified(email)).isFalse();
    }

    @Test
    void 인증번호_생성기가_호출되는지_확인할_수_있다() {
        // given
        String email = "test@example.com";
        String customCode = "999999";
        fakeVerificationCodeGenerator.setFixedCode(customCode);
        fakeVerificationCodeGenerator.resetCallCount();
        EmailVerificationRequestDto requestDto = new EmailVerificationRequestDto(email);

        // when
        emailVerificationService.sendVerificationCode(requestDto);

        // then
        assertThat(fakeVerificationCodeGenerator.getCallCount()).isEqualTo(1);
        EmailVerification verification = fakeEmailVerificationRepository.findLatestByEmail(email).get();
        assertThat(verification.getVerificationCode()).isEqualTo(customCode);
    }

    @Test
    void 여러_번_전송시_인증번호_생성기가_매번_호출된다() {
        // given
        String email = "test@example.com";
        EmailVerificationRequestDto requestDto = new EmailVerificationRequestDto(email);
        fakeVerificationCodeGenerator.resetCallCount();

        // when
        emailVerificationService.sendVerificationCode(requestDto);
        emailVerificationService.sendVerificationCode(requestDto);
        emailVerificationService.resendVerificationCode(requestDto);

        // then
        assertThat(fakeVerificationCodeGenerator.getCallCount()).isEqualTo(3);
    }
}
