package com.iny.side.assignment.web.dto;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.submission.domain.entity.Submission;

import java.time.LocalDateTime;

public record StudentAssignmentSimpleResponseDto(Long id, String title, LocalDateTime dueDate, String objective, Submission.SubmissionStatus status) {
    public static StudentAssignmentSimpleResponseDto from(Assignment assignment, Submission.SubmissionStatus status) {
        return new StudentAssignmentSimpleResponseDto(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDueDate(),
                assignment.getObjective(),
                status != null ? status : Submission.SubmissionStatus.NOT_STARTED
        );
    }
}
