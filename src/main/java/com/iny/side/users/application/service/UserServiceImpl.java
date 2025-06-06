package com.iny.side.users.application.service;

import com.iny.side.common.exception.DuplicateUsernameException;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.domain.repository.UserRepository;
import com.iny.side.users.web.dto.SignupDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Account signup(SignupDto signupDto) {
        if (existsByUsername(signupDto.username())) {
            throw new DuplicateUsernameException(signupDto.username());
        }

        return userRepository.save(signupDto.toAccount(passwordEncoder.encode(signupDto.password())));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
