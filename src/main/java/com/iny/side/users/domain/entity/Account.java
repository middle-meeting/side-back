package com.iny.side.users.domain.entity;

import com.iny.side.users.domain.Role;
import com.iny.side.users.web.dto.AccountDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Account(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static Account from(AccountDto accountDto, String cryptedPassword) {
        return Account.builder().id(accountDto.getId())
                .password(cryptedPassword)
                .username(accountDto.getUsername())
                .role(accountDto.getRole()).build();
    }
}
