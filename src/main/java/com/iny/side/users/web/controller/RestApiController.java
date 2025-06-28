package com.iny.side.users.web.controller;


import com.iny.side.common.BasicResponse;
import com.iny.side.users.application.service.EmailVerificationService;
import com.iny.side.users.application.service.UserService;
import com.iny.side.users.web.dto.AccountResponseDto;
import com.iny.side.users.web.dto.EmailVerificationConfirmDto;
import com.iny.side.users.web.dto.EmailVerificationConfirmResponseDto;
import com.iny.side.users.web.dto.EmailVerificationRequestDto;
import com.iny.side.users.web.dto.EmailVerificationResponseDto;
import com.iny.side.users.web.dto.SignupDto;
import com.iny.side.users.web.dto.SignupResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BasicResponse<SignupResponseDto>> signup(@RequestBody @Valid SignupDto signupDto) {
        SignupResponseDto response = SignupResponseDto.from(userService.signup(signupDto));
        return ResponseEntity.ok(BasicResponse.ok(response));
    }


    @PostMapping("/signup/send-verification")
    public ResponseEntity<BasicResponse<EmailVerificationResponseDto>> sendVerificationCode(@RequestBody @Valid EmailVerificationRequestDto requestDto) {
        emailVerificationService.sendVerificationCode(requestDto);
        EmailVerificationResponseDto response = EmailVerificationResponseDto.success();
        return ResponseEntity.ok(BasicResponse.ok(response));
    }

    @PostMapping("/signup/verify-email")
    public ResponseEntity<BasicResponse<EmailVerificationConfirmResponseDto>> verifyEmail(@RequestBody @Valid EmailVerificationConfirmDto confirmDto) {
        boolean verified = emailVerificationService.verifyCode(confirmDto);
        EmailVerificationConfirmResponseDto response = verified
            ? EmailVerificationConfirmResponseDto.success()
            : EmailVerificationConfirmResponseDto.failure();
        return ResponseEntity.ok(BasicResponse.ok(response));
    }

    @PostMapping("/signup/resend-verification")
    public ResponseEntity<BasicResponse<EmailVerificationResponseDto>> resendVerificationCode(@RequestBody @Valid EmailVerificationRequestDto requestDto) {
        emailVerificationService.resendVerificationCode(requestDto);
        EmailVerificationResponseDto response = EmailVerificationResponseDto.resendSuccess();
        return ResponseEntity.ok(BasicResponse.ok(response));
    }

}