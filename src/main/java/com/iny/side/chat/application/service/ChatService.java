package com.iny.side.chat.application.service;

import com.iny.side.chat.web.dto.ChatResponseDto;

public interface ChatService {

    ChatResponseDto sendMessage(Long studentId, Long assignmentId, String message);
}
