package com.iny.side.submission.web.dto;

import com.iny.side.submission.domain.entity.Submission;

import java.time.LocalDateTime;
import java.util.List;

public record SubmissionResponseDto(
    Long id,
    String primaryDiagnosis,
    String subDiagnosis,
    List<PrescriptionResponseDto> prescriptions,
    String finalJudgment,
    LocalDateTime submittedAt,
    String status
) {
    
    public static SubmissionResponseDto from(Submission submission, List<PrescriptionResponseDto> prescriptions) {
        return new SubmissionResponseDto(
            submission.getId(),
            submission.getPrimaryDiagnosis(),
            submission.getSubDiagnosis(),
            prescriptions,
            submission.getFinalJudgment(),
            submission.getSubmittedAt(),
            submission.getStatus().name()
        );
    }
}
