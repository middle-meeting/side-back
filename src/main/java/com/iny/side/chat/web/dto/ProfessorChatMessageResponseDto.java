package com.iny.side.chat.web.dto;

import com.iny.side.chat.domain.entity.ChatMessage;

import java.time.LocalDateTime;

public record ProfessorChatMessageResponseDto(
        Long id,
        Integer turnNumber,
        String speaker,
        String message,
        Integer score,
        String feedback,
        LocalDateTime timestamp
) {

    public static ProfessorChatMessageResponseDto from(ChatMessage chatMessage) {
        return new ProfessorChatMessageResponseDto(
                chatMessage.getId(),
                chatMessage.getTurnNumber(),
                chatMessage.getSpeaker().name(),
                chatMessage.getMessage(),
                chatMessage.getScore(),
                chatMessage.getFeedback(),
                chatMessage.getTimestamp()
        );
    }
}
