package com.iny.side.chat.web.dto;

import com.iny.side.chat.domain.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponseDto(
    Long id,
    Integer turnNumber,
    String speaker,
    String message,
    LocalDateTime timestamp
) {
    
    public static ChatMessageResponseDto from(ChatMessage chatMessage) {
        return new ChatMessageResponseDto(
            chatMessage.getId(),
            chatMessage.getTurnNumber(),
            chatMessage.getSpeaker().name(),
            chatMessage.getMessage(),
            chatMessage.getTimestamp()
        );
    }
}
