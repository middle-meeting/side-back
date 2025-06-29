package com.iny.side.users.web.dto;

public record EmailVerificationConfirmResponseDto(
    String message
) {

    public static EmailVerificationConfirmResponseDto success() {
        return new EmailVerificationConfirmResponseDto("이메일 인증이 완료되었습니다.");
    }
}
