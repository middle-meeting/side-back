package com.iny.side.users.infrastructure.repository;

import com.iny.side.users.domain.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.iny.side.users.domain.repository.UserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<Account> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public Account save(Account account) {
        return userJpaRepository.save(account);
    }
}
