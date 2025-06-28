package com.iny.side.chat.application.service;

import com.iny.side.chat.web.dto.ChatMessageResponseDto;
import com.iny.side.chat.web.dto.ChatResponseDto;

import java.util.List;

public interface ChatService {

    ChatResponseDto sendMessage(Long studentId, Long assignmentId, String message);

    List<ChatMessageResponseDto> getMessages(Long studentId, Long assignmentId);
}
