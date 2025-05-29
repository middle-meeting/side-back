package com.iny.side.assignment.web.dto;

import com.iny.side.assignment.domain.entity.Assignment;

import java.time.LocalDateTime;

public record AssignmentDto(Long id, String title, LocalDateTime dueDate, String creatorName) {
    public static AssignmentDto from(Assignment assignment) {
        return new AssignmentDto(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDueDate(),
                assignment.getAccount().getName()
        );
    }
}
