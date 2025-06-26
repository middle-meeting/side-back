package com.iny.side.users.web.controller;


import com.iny.side.users.application.service.EmailVerificationService;
import com.iny.side.users.application.service.UserService;
import com.iny.side.users.web.dto.AccountResponseDto;
import com.iny.side.users.web.dto.EmailVerificationConfirmDto;
import com.iny.side.users.web.dto.EmailVerificationRequestDto;
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
    private final EmailVerificationService emailVerificationService;

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

    @PostMapping("/signup/send-verification")
    public Map<String, String> sendVerificationCode(@RequestBody @Valid EmailVerificationRequestDto requestDto) {
        emailVerificationService.sendVerificationCode(requestDto);
        return Map.of("message", "인증번호가 전송되었습니다.");
    }

    @PostMapping("/signup/verify-email")
    public Map<String, Boolean> verifyEmail(@RequestBody @Valid EmailVerificationConfirmDto confirmDto) {
        boolean verified = emailVerificationService.verifyCode(confirmDto);
        return Map.of("verified", verified);
    }

    @PostMapping("/signup/resend-verification")
    public Map<String, String> resendVerificationCode(@RequestBody @Valid EmailVerificationRequestDto requestDto) {
        emailVerificationService.resendVerificationCode(requestDto);
        return Map.of("message", "인증번호가 재전송되었습니다.");
    }

}