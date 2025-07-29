package com.iny.side.chat.application.service;

import com.iny.side.chat.web.dto.ProfessorChatMessageResponseDto;

import java.util.List;

public interface ProfessorChatService {
    List<ProfessorChatMessageResponseDto> getMessages(Long professorId, Long assignmentId, Long studentId);
}
