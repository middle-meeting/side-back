package com.iny.side.chat.web.dto;

import java.util.List;

public record ChatResponseDto(
    ChatMessageResponseDto studentMessage,
    ChatMessageResponseDto aiMessage
) {
    
    public static ChatResponseDto of(ChatMessageResponseDto studentMessage, ChatMessageResponseDto aiMessage) {
        return new ChatResponseDto(studentMessage, aiMessage);
    }
}
