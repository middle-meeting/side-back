package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupDto(
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String username,

        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
        String password,
        @NotBlank(message = "이름이 입력되지 않았습니다.")
        @Size(min = 2, message = "이름이 올바르지 않습니다.")
        String name,
        Role role
) {
    public static SignupDto from(Account account) {
        return new SignupDto(
                account.getUsername(),
                account.getPassword(),
                account.getName(),
                account.getRole()
        );
    }
}
