package com.iny.side.submission.web.dto;

public record SubmissionStatusResponseDto(
        Long enrolledCount,
        Long evaluatedCount,
        Long evaluationRequiredCount,
        Long notSubmittedCount
) {
}
