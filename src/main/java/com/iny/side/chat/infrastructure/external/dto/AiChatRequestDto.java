package com.iny.side.chat.infrastructure.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiChatRequestDto(
    String message,

    @JsonProperty("patient_context")
    String patientContext
) {

    public static AiChatRequestDto of(String message, String patientContext) {
        return new AiChatRequestDto(message, patientContext);
    }
}
