package com.iny.side.chat.domain.repository;

import com.iny.side.chat.domain.entity.ChatMessage;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository {
    
    ChatMessage save(ChatMessage chatMessage);
    
    List<ChatMessage> findBySubmissionIdOrderByTurnNumber(Long submissionId);
    
    Integer findMaxTurnNumberBySubmissionId(Long submissionId);

    Optional<ChatMessage> findById(Long chatMessageId);
}
