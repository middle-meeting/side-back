package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountDto {
    private final Long id;
    private final String username;
    private final String password;
    private final Role role;

    @Builder
    public AccountDto(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
