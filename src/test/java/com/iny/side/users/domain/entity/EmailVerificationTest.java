package com.iny.side.users.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerificationTest {

    @Test
    void 만료시간이_지나면_만료된_것으로_판단한다() {
        // given
        LocalDateTime pastTime = LocalDateTime.now().minusMinutes(4);
        EmailVerification emailVerification = EmailVerification.builder()
                .email("test@example.com")
                .verificationCode("123456")
                .createdAt(pastTime)
                .expiresAt(pastTime.plusMinutes(3))
                .build();

        // when & then
        assertThat(emailVerification.isExpired()).isTrue();
    }

    @Test
    void 만료시간이_지나지_않으면_유효한_것으로_판단한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification = EmailVerification.builder()
                .email("test@example.com")
                .verificationCode("123456")
                .createdAt(now)
                .expiresAt(now.plusMinutes(3))
                .build();

        // when & then
        assertThat(emailVerification.isExpired()).isFalse();
    }

    @Test
    void 인증을_완료할_수_있다() {
        // given
        EmailVerification emailVerification = EmailVerification.builder()
                .email("test@example.com")
                .verificationCode("123456")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(3))
                .build();

        // when
        emailVerification.verify();

        // then
        assertThat(emailVerification.isVerified()).isTrue();
        assertThat(emailVerification.getVerified()).isTrue();
    }

    @Test
    void 초기_생성시_인증_상태는_false이다() {
        // given & when
        EmailVerification emailVerification = EmailVerification.builder()
                .email("test@example.com")
                .verificationCode("123456")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(3))
                .build();

        // then
        assertThat(emailVerification.isVerified()).isFalse();
    }
}
