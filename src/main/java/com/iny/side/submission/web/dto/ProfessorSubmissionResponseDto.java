package com.iny.side.submission.web.dto;

import com.iny.side.submission.domain.entity.Submission;

import java.time.LocalDateTime;
import java.util.List;

public record ProfessorSubmissionResponseDto(
        Long id,
        String name,
        String studentId,
        String primaryDiagnosis,
        String subDiagnosis,
        List<PrescriptionResponseDto> prescriptions,
        String finalJudgment,
        LocalDateTime submittedAt,
        String status
) {

    public static ProfessorSubmissionResponseDto from(Submission submission, List<PrescriptionResponseDto> prescriptions) {
        return new ProfessorSubmissionResponseDto(
                submission.getId(),
                submission.getStudent().getName(),
                submission.getStudent().getStudentId(),
                submission.getPrimaryDiagnosis(),
                submission.getSubDiagnosis(),
                prescriptions,
                submission.getFinalJudgment(),
                submission.getSubmittedAt(),
                submission.getStatus().name()
        );
    }
}
