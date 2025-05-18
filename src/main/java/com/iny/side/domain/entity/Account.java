package com.iny.side.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private int age;
    @Enumerated(EnumType.STRING)
    private Role role;
}
