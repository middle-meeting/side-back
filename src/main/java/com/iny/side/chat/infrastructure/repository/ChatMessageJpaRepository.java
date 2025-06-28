package com.iny.side.chat.infrastructure.repository;

import com.iny.side.chat.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findBySubmissionIdOrderByTurnNumber(Long submissionId);
    
    @Query("SELECT COALESCE(MAX(c.turnNumber), 0) FROM ChatMessage c WHERE c.submission.id = :submissionId")
    Integer findMaxTurnNumberBySubmissionId(@Param("submissionId") Long submissionId);
}
