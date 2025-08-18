package com.iny.side.submission.domain.entity;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.submission.exception.InvalidSubmissionDataException;
import com.iny.side.users.domain.entity.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Account student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Column(name = "primary_diagnosis")
    private String primaryDiagnosis;

    @Column(name = "sub_diagnosis")
    private String subDiagnosis;

    @Column(name = "final_judgment", columnDefinition = "TEXT")
    private String finalJudgment;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubmissionStatus status = SubmissionStatus.DRAFT;

    @Builder
    public Submission(Long id, Account student, Assignment assignment,
                      String primaryDiagnosis, String subDiagnosis,
                      String finalJudgment, LocalDateTime submittedAt,
                      SubmissionStatus status) {
        this.id = id;
        this.student = student;
        this.assignment = assignment;
        this.primaryDiagnosis = primaryDiagnosis;
        this.subDiagnosis = subDiagnosis;
        this.finalJudgment = finalJudgment;
        this.submittedAt = submittedAt;
        this.status = status != null ? status : SubmissionStatus.DRAFT;
    }

    public void submit(String primaryDiagnosis, String subDiagnosis, String finalJudgment) {
        validateSubmissionData(primaryDiagnosis, finalJudgment);

        this.primaryDiagnosis = primaryDiagnosis;
        this.subDiagnosis = subDiagnosis;
        this.finalJudgment = finalJudgment;
        this.submittedAt = LocalDateTime.now();
        this.status = SubmissionStatus.SUBMITTED;
    }

    private void validateSubmissionData(String primaryDiagnosis, String finalJudgment) {
        if (primaryDiagnosis == null || primaryDiagnosis.trim().isEmpty()) {
            throw new InvalidSubmissionDataException("주진단");
        }

        if (finalJudgment == null || finalJudgment.trim().isEmpty()) {
            throw new InvalidSubmissionDataException("최종 판단");
        }
    }

    public enum SubmissionStatus {
        NOT_STARTED, // 진행전
        DRAFT,       // 진행 중
        SUBMITTED    // 제출 완료
    }
}