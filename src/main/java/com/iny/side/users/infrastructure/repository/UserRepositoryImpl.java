package com.iny.side.users.infrastructure.repository;

import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<Account> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }
}
