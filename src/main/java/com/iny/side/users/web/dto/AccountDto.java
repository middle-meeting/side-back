package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import lombok.Builder;

@Builder
public record AccountDto(Long id, String username, String password, String name, Role role) {
    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .name(account.getName())
                .role(account.getRole())
                .build();
    }
}
