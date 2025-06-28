package com.iny.side.chat.infrastructure.repository;

import com.iny.side.chat.domain.entity.ChatMessage;
import com.iny.side.chat.domain.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {
    
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    
    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageJpaRepository.save(chatMessage);
    }
    
    @Override
    public List<ChatMessage> findBySubmissionIdOrderByTurnNumber(Long submissionId) {
        return chatMessageJpaRepository.findBySubmissionIdOrderByTurnNumber(submissionId);
    }
    
    @Override
    public Integer findMaxTurnNumberBySubmissionId(Long submissionId) {
        return chatMessageJpaRepository.findMaxTurnNumberBySubmissionId(submissionId);
    }
}
