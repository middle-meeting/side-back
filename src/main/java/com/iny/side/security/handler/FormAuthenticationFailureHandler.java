package com.iny.side.security.handler;

import com.iny.side.security.exception.SecretException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class FormAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, IOException {
        String errorMessage = "Invalid username or password";

        if (exception instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Username not exists";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "Expired password";
        } else if (exception instanceof SecretException) {
            errorMessage = "Invalid Secret key";
        }

        // URL 파라미터로 전달할 수 있도록 인코딩(한글이나 특수문자가 에러메세지에 들어갈 때를 대비)
        String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        setDefaultFailureUrl("/login?error=true&exception=" + encodedMessage);  // 컨트롤러에서 해당 요청에 대한 처리 필요

        super.onAuthenticationFailure(request, response, exception);
    }
}
