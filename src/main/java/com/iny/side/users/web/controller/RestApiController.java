package com.iny.side.users.web.controller;


import com.iny.side.users.application.service.UserService;
import com.iny.side.users.web.dto.AccountResponseDto;
import com.iny.side.users.web.dto.SignupDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestApiController {

    private final UserService userService;

    @GetMapping(value = "/student")
    public AccountResponseDto student(@AuthenticationPrincipal AccountResponseDto accountResponseDto) {
        return accountResponseDto;
    }

    @GetMapping(value = "/professor")
    public AccountResponseDto professor(@AuthenticationPrincipal AccountResponseDto accountResponseDto) {
        return accountResponseDto;
    }

    @GetMapping(value = "/admin")
    public AccountResponseDto admin(@AuthenticationPrincipal AccountResponseDto accountResponseDto) {
        return accountResponseDto;
    }

    @PostMapping("/signup")
    public SignupDto signup(@RequestBody @Valid SignupDto signupDto) {
        return SignupDto.from(userService.signup(signupDto));
    }

    @GetMapping("/signup/check-username")
    public Map<String, Boolean> checkUsername(@RequestParam(value = "username") String username) {
        return Map.of("exists", userService.existsByUsername(username));
    }

}