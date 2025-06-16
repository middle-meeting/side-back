package com.iny.side.assignment.web.dto;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.common.domain.GenderType;

import java.time.LocalDateTime;

public record ProfessorAssignmentDetailResponseDto(
        Long id,
        String title,
        String personaName,
        Integer personaAge,
        GenderType personaGender,
        String personaSymptom,
        String personaHistory,
        String personaPersonality,
        String personaDisease,
        String objective,
        Integer maxTurns,
        LocalDateTime dueDate,
        String courseName,
        String semester,
        String professorName) {
    public static ProfessorAssignmentDetailResponseDto from(Assignment assignment) {
        return new ProfessorAssignmentDetailResponseDto(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getPersonaName(),
                assignment.getPersonaAge(),
                assignment.getPersonaGender(),
                assignment.getPersonaSymptom(),
                assignment.getPersonaHistory(),
                assignment.getPersonaPersonality(),
                assignment.getPersonaDisease(),
                assignment.getObjective(),
                assignment.getMaxTurns(),
                assignment.getDueDate(),
                assignment.getCourse().getName(),
                assignment.getCourse().getSemester(),
                assignment.getCourse().getAccount().getName()
        );
    }
}
