package com.iny.side.assignment.web.dto;

import com.iny.side.assignment.domain.entity.Assignment;

import java.time.LocalDateTime;

public record AssignmentResponseDto(Long id, String title, LocalDateTime dueDate, String creatorName) {
    public static AssignmentResponseDto from(Assignment assignment) {
        return new AssignmentResponseDto(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDueDate(),
                assignment.getAccount().getName()
        );
    }
}
