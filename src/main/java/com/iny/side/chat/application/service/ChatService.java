package com.iny.side.chat.application.service;

import com.iny.side.chat.web.dto.ChatMessageRequestDto;
import com.iny.side.chat.web.dto.ChatResponseDto;

public interface ChatService {
    
    ChatResponseDto sendMessage(Long studentId, ChatMessageRequestDto requestDto);
}
