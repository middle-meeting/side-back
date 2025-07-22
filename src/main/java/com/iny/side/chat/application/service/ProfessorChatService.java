package com.iny.side.chat.application.service;

import com.iny.side.chat.web.dto.ChatMessageResponseDto;

import java.util.List;

public interface ProfessorChatService {
    List<ChatMessageResponseDto> getMessages(Long professorId, Long assignmentId, Long studentId);
}
