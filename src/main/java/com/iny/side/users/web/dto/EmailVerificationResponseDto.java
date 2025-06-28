package com.iny.side.users.web.dto;

public record EmailVerificationResponseDto(
    String message
) {
    
    public static EmailVerificationResponseDto success() {
        return new EmailVerificationResponseDto("인증번호가 전송되었습니다.");
    }
    
    public static EmailVerificationResponseDto resendSuccess() {
        return new EmailVerificationResponseDto("인증번호가 재전송되었습니다.");
    }
}
