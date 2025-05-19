package com.iny.side.users.application.service;

import com.iny.side.users.web.dto.SignupDto;

public interface UserService {
    void createUser(SignupDto signupDto);
}
