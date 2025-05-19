package com.iny.side.users.infrastructure.repository;

import com.iny.side.users.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
}
