package com.iny.side.evaluation.domain.entity;

import com.iny.side.evaluation.web.dto.EvaluationRequestDto;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.users.domain.entity.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "feedback", nullable = false, columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "is_visible")
    private Boolean isVisible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account professor;

    @Builder
    public Evaluation(Long id, Integer score, String feedback, Boolean isVisible, Submission submission, Account professor) {
        this.id = id;
        this.score = score;
        this.feedback = feedback;
        this.isVisible = isVisible;
        this.submission = submission;
        this.professor = professor;
    }

    public static Evaluation create(Submission submission, Account professor, EvaluationRequestDto evaluationRequestDto) {
        return Evaluation.builder()
                .score(evaluationRequestDto.score())
                .feedback(evaluationRequestDto.feedback())
                .isVisible(Boolean.FALSE)
                .submission(submission)
                .professor(professor)
                .build();
    }
}
