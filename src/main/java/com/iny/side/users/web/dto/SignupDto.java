package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import lombok.Builder;

@Builder
public record SignupDto(String username, String password, Role role) {
    public static SignupDto from(Account account) {
        return SignupDto.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .role(account.getRole())
                .build();
    }
}
