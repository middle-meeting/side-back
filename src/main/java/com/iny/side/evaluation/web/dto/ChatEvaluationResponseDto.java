package com.iny.side.evaluation.web.dto;

import com.iny.side.chat.domain.entity.ChatMessage;

public record ChatEvaluationResponseDto(
        long id,
        int score,
        String feedback
) {

    public static ChatEvaluationResponseDto from(ChatMessage chatMessage) {
        return new ChatEvaluationResponseDto(
                chatMessage.getId(),
                chatMessage.getScore(),
                chatMessage.getFeedback()
        );
    }
}
