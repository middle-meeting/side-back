package com.iny.side.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iny.side.common.BasicResponse;
import com.iny.side.common.ErrorDetail;
import com.iny.side.common.ErrorPayload;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.List;

public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("FORBIDDEN")
                .message("접근 권한이 없습니다.")
                .build();
        ErrorPayload payload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();
        BasicResponse<?> body = BasicResponse.error(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", payload);

        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
