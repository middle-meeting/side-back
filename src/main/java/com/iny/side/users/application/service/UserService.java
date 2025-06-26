package com.iny.side.users.application.service;

import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.web.dto.SignupDto;

public interface UserService {
    Account signup(SignupDto signupDto);

    boolean existsByUsername(String username);

    void validateSignupData(SignupDto signupDto);
}
