package com.iny.side.users.application.service;

import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.infrastructure.repository.UserRepository;
import com.iny.side.users.web.dto.AccountDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void createUser(AccountDto accountDto) {
        userRepository.save(Account.from(accountDto, passwordEncoder.encode(accountDto.getPassword())));
    }
}
