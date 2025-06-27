package com.iny.side.assignment.web.dto;

import com.iny.side.assignment.domain.entity.Assignment;

import java.time.LocalDateTime;

public record AssignmentSimpleResponseDto(Long id, String title, LocalDateTime dueDate, String objective) {
    public static AssignmentSimpleResponseDto from(Assignment assignment) {
        return new AssignmentSimpleResponseDto(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDueDate(),
                assignment.getObjective()
        );
    }
}
