package com.iny.side.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iny.side.common.BasicResponse;
import com.iny.side.common.ErrorDetail;
import com.iny.side.common.ErrorPayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component("restFailureHandler")
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        String errorMessage;
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            errorMessage = "이메일 또는 비밀번호가 올바르지 않습니다.";
        } else {
            errorMessage = "로그인에 실패했습니다.";
        }

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("AUTHENTICATION_FAILED")
                .message(errorMessage)
                .build();

        ErrorPayload errorPayload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();

        BasicResponse<?> basicResponse = BasicResponse.error(
                HttpStatus.UNAUTHORIZED,
                errorMessage,
                errorPayload
        );

        mapper.writeValue(response.getWriter(), basicResponse);
    }
}
