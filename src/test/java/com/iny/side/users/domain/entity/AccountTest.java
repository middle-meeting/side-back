package com.iny.side.users.domain.entity;

import com.iny.side.users.domain.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void 이메일_인증_상태를_변경할_수_있다() {
        // given
        Account account = Account.builder()
                .username("test@test.com")
                .password("password123")
                .name("테스트")
                .role(Role.STUDENT)
                .school("테스트대학교")
                .major("컴퓨터공학과")
                .grade(1)
                .studentId("20241234")
                .emailVerified(false)
                .build();

        // when
        account.verifyEmail();

        // then
        assertThat(account.getEmailVerified()).isTrue();
    }

    @Test
    void emailVerified가_null이면_기본값_false로_설정된다() {
        // given & when
        Account account = Account.builder()
                .username("test@test.com")
                .password("password123")
                .name("테스트")
                .role(Role.STUDENT)
                .school("테스트대학교")
                .major("컴퓨터공학과")
                .grade(1)
                .studentId("20241234")
                .emailVerified(null)
                .build();

        // then
        assertThat(account.getEmailVerified()).isFalse();
    }
}
