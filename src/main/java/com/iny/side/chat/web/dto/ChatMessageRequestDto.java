package com.iny.side.chat.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequestDto(
    @NotNull(message = "과제 ID는 필수입니다")
    Long assignmentId,
    
    @NotBlank(message = "메시지는 필수입니다")
    String message
) {
}
