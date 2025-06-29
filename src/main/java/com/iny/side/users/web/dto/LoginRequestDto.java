package com.iny.side.users.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String username,
        
        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        String password
) {
}
