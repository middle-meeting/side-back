package com.iny.side.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iny.side.common.response.BasicResponse;
import com.iny.side.common.response.ErrorDetail;
import com.iny.side.common.response.ErrorPayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RestErrorResponder {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void send(HttpServletResponse response, HttpStatus status, String code, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code(code)
                .message(message)
                .build();
        ErrorPayload payload = ErrorPayload.builder()
                .errors(List.of(errorDetail))
                .build();
        BasicResponse<?> body = BasicResponse.error(status, message, payload);
        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
