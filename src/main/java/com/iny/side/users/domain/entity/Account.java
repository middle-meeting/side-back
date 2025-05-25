package com.iny.side.users.domain.entity;

import com.iny.side.users.domain.Role;
import com.iny.side.users.web.dto.SignupDto;
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
    @Column(name = "account_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Account(Long id, String username, String password, String name, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static Account from(SignupDto signupDto, String cryptedPassword) {
        return Account.builder()
                .password(cryptedPassword)
                .username(signupDto.username())
                .name(signupDto.name())
                .role(signupDto.role())
                .build();
    }
}
