package com.iny.side.evaluation.web.dto;

import com.iny.side.chat.domain.entity.ChatMessage;
import com.iny.side.chat.web.dto.ChatMessageResponseDto;

public record ChatFeedbackResultDto(
        ChatMessageResponseDto studentMessage,
        ChatMessageResponseDto aiMessage,
        Integer score,
        String feedback

) {

    public static ChatFeedbackResultDto from(ChatMessage student, ChatMessage ai) {
        return new ChatFeedbackResultDto(
                ChatMessageResponseDto.from(student),
                ChatMessageResponseDto.from(ai),
                student.getScore(),
                student.getFeedback()
        );
    }
}
