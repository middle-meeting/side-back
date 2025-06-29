package com.iny.side.users.web.dto;

public record CsrfTokenDto(
        String token,
        String headerName,
        String parameterName
) {
}
