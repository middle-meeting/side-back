package com.iny.side.users.web.controller;


import com.iny.side.common.BasicResponse;
import com.iny.side.users.application.service.EmailVerificationService;
import com.iny.side.users.application.service.UserService;
import com.iny.side.users.web.dto.*;
import com.iny.side.users.web.dto.AccountResponseDto;
import com.iny.side.users.web.dto.EmailVerificationConfirmDto;
import com.iny.side.users.web.dto.EmailVerificationConfirmResponseDto;
import com.iny.side.users.web.dto.EmailVerificationRequestDto;
import com.iny.side.users.web.dto.EmailVerificationResponseDto;
import com.iny.side.users.web.dto.SignupDto;
import com.iny.side.users.web.dto.SignupResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

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
        emailVerificationService.verifyCode(confirmDto);
        EmailVerificationConfirmResponseDto response = EmailVerificationConfirmResponseDto.success();
        return ResponseEntity.ok(BasicResponse.ok(response));
    }

    @PostMapping("/signup/resend-verification")
    public ResponseEntity<BasicResponse<EmailVerificationResponseDto>> resendVerificationCode(@RequestBody @Valid EmailVerificationRequestDto requestDto) {
        emailVerificationService.resendVerificationCode(requestDto);
        EmailVerificationResponseDto response = EmailVerificationResponseDto.resendSuccess();
        return ResponseEntity.ok(BasicResponse.ok(response));
    }

    @GetMapping("/me")
    public ResponseEntity<BasicResponse<LoginResponseDto>> getCurrentUser(@AuthenticationPrincipal AccountResponseDto accountResponseDto) {
        LoginResponseDto response = LoginResponseDto.from(accountResponseDto);
        return ResponseEntity.ok(BasicResponse.ok(response));
    }

    @GetMapping("/csrf")
    public ResponseEntity<BasicResponse<CsrfTokenDto>> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken == null) {
            csrfToken = (CsrfToken) request.getAttribute("_csrf");
        }

        CsrfTokenDto tokenDto = new CsrfTokenDto(
                csrfToken.getToken(),
                csrfToken.getHeaderName(),
                csrfToken.getParameterName()
        );

        return ResponseEntity.ok(BasicResponse.ok(tokenDto));
    }

}