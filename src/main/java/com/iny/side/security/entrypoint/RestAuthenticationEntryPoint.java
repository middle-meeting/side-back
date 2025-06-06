package com.iny.side.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iny.side.common.BasicResponse;
import com.iny.side.common.ErrorDetail;
import com.iny.side.common.ErrorPayload;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.List;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("UNAUTHORIZED")
                .message("인증이 필요합니다.")
                .build();
        ErrorPayload payload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();
        BasicResponse<?> body = BasicResponse.error(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", payload);

        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
