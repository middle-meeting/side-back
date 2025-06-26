package com.iny.side.users.application.service;

import com.iny.side.common.exception.DuplicateUsernameException;
import com.iny.side.common.exception.EmailNotVerifiedException;
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
    private final EmailVerificationService emailVerificationService;

    @Override
    @Transactional
    public Account signup(SignupDto signupDto) {
        if (existsByUsername(signupDto.username())) {
            throw new DuplicateUsernameException(signupDto.username());
        }

        // 이메일 인증 확인
        if (!emailVerificationService.isEmailVerified(signupDto.username())) {
            throw new EmailNotVerifiedException(signupDto.username());
        }

        Account account = signupDto.toAccount(passwordEncoder.encode(signupDto.password()));
        account.verifyEmail(); // 이메일 인증 상태로 설정

        return userRepository.save(account);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


}
