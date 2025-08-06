package com.iny.side.submission.domain.vo;

import java.time.LocalDateTime;

public record SubmissionDetailVo(
        Long accountId,
        String studentName,
        String studentId,
        String userName,
        Long submissionId,
        LocalDateTime submittedAt,
        Long turns,
        Integer score,
        String primaryDiagnosis
) {
}
