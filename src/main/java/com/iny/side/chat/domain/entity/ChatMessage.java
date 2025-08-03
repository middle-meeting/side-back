package com.iny.side.chat.domain.entity;

import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.submission.domain.entity.Submission;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @Column(name = "turn_number", nullable = false)
    private Integer turnNumber;

    @Column(name = "speaker", nullable = false)
    @Enumerated(EnumType.STRING)
    private SpeakerType speaker;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "score")
    private Integer score;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Builder
    public ChatMessage(Long id, Submission submission, Integer turnNumber, 
                      SpeakerType speaker, String message, Integer score, String feedback, LocalDateTime timestamp) {
        this.id = id;
        this.submission = submission;
        this.turnNumber = turnNumber;
        this.speaker = speaker;
        this.message = message;
        this.score = score;
        this.feedback = feedback;
        this.timestamp = timestamp;
    }

    public enum SpeakerType {
        STUDENT,
        AI;

        public String toGemmaRole() {
            return switch (this) {
                case STUDENT -> "user";
                case AI      -> "assistant";
            };
        }
    }

    public void validateEvaluable() {
        if (this.speaker.equals(SpeakerType.AI)) {
            throw new ForbiddenException("채점은 학생의 대화에만 가능");
        }
    }

    public void evaluate(int score, String feedback) {
        this.score = score;
        this.feedback = feedback;
    }
}