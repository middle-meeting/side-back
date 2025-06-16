package com.iny.side.assignment.web.dto;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.common.domain.GenderType;

import java.time.LocalDateTime;

public record StudentAssignmentDetailResponseDto(
        Long id,
        String title,
        String personaName,
        Integer personaAge,
        GenderType personaGender,
        String personaSymptom,
        String personaHistory,
        Integer maxTurns,
        LocalDateTime dueDate,
        String courseName,
        String semester,
        String professorName) {
    public static StudentAssignmentDetailResponseDto from(Assignment assignment) {
        return new StudentAssignmentDetailResponseDto(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getPersonaName(),
                assignment.getPersonaAge(),
                assignment.getPersonaGender(),
                assignment.getPersonaSymptom(),
                assignment.getPersonaHistory(),
                assignment.getMaxTurns(),
                assignment.getDueDate(),
                assignment.getCourse().getName(),
                assignment.getCourse().getSemester(),
                assignment.getCourse().getAccount().getName()
        );
    }
}
