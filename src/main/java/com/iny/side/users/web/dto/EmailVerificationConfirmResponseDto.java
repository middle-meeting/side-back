package com.iny.side.users.web.dto;

public record EmailVerificationConfirmResponseDto(
    boolean verified,
    String message
) {
    
    public static EmailVerificationConfirmResponseDto success() {
        return new EmailVerificationConfirmResponseDto(true, "이메일 인증이 완료되었습니다.");
    }
    
    public static EmailVerificationConfirmResponseDto failure() {
        return new EmailVerificationConfirmResponseDto(false, "인증번호가 올바르지 않거나 만료되었습니다.");
    }
}
