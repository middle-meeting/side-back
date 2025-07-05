package com.iny.side.chat.infrastructure.external.dto;

import com.iny.side.chat.domain.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageSimpleDto(
    String role,
    String content
) {
    
    public static ChatMessageSimpleDto from(ChatMessage chatMessage) {
        return new ChatMessageSimpleDto(
            chatMessage.getSpeaker().toGemmaRole(),
            chatMessage.getMessage()
        );
    }
}
