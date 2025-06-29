package com.iny.side.users.web.dto;

public record LogoutResponseDto(
        String message
) {
    
    public static LogoutResponseDto success() {
        return new LogoutResponseDto("로그아웃이 완료되었습니다.");
    }
}
