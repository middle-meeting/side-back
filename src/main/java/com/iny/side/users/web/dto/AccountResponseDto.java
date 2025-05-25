package com.iny.side.users.web.dto;

import com.iny.side.users.domain.Role;

public record AccountResponseDto(Long id, String username, String name, Role role) {
    public static AccountResponseDto from(AccountDto accountDto) {
        return new AccountResponseDto(
                accountDto.id(),
                accountDto.username(),
                accountDto.name(),
                accountDto.role());
    }
}
