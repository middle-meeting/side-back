package com.iny.side.submission.web.dto;

import com.iny.side.submission.domain.vo.SubmissionDetailVo;

import java.time.LocalDateTime;
import java.util.List;

public record ProfessorSubmissionDetailResponseDto(
        Long accountId,
        String studentName,
        String studentId,
        String email,
        SubmissionFilter status,
        LocalDateTime submittedAt,
        Long turns,
        Integer score,
        String primaryDiagnosis,
        List<PrescriptionResponseDto> prescriptions
) {

    public static ProfessorSubmissionDetailResponseDto from(SubmissionDetailVo submissionDetailVo, List<PrescriptionResponseDto> prescriptions) {
        return new ProfessorSubmissionDetailResponseDto(
                submissionDetailVo.accountId(),
                submissionDetailVo.studentName(),
                submissionDetailVo.studentId(),
                submissionDetailVo.userName(),
                SubmissionFilter.from(submissionDetailVo),
                submissionDetailVo.submittedAt(),
                submissionDetailVo.turns(),
                submissionDetailVo.score(),
                submissionDetailVo.primaryDiagnosis(),
                prescriptions
        );
    }
}
