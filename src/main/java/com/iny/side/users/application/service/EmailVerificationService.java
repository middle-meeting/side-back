package com.iny.side.users.application.service;

import com.iny.side.users.web.dto.EmailVerificationConfirmDto;
import com.iny.side.users.web.dto.EmailVerificationRequestDto;

public interface EmailVerificationService {
    void sendVerificationCode(EmailVerificationRequestDto requestDto);

    void verifyCode(EmailVerificationConfirmDto confirmDto);

    boolean isEmailVerified(String email);
}
