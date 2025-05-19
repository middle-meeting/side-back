package com.iny.side.users.application.service;

import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.infrastructure.repository.UserJpaRepository;
import com.iny.side.users.web.dto.SignupDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public void createUser(SignupDto signupDto) {
        userJpaRepository.save(Account.from(signupDto, passwordEncoder.encode(signupDto.password())));
    }
}
