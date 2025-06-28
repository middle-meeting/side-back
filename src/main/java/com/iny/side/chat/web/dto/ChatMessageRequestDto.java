package com.iny.side.chat.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequestDto(
    @NotBlank(message = "메시지는 필수입니다")
    String message
) {
}
