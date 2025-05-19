package com.iny.side.users.domain.repository;

import com.iny.side.users.domain.entity.Account;

import java.util.Optional;

public interface UserRepository {
    Optional<Account> findByUsername(String username);
}
