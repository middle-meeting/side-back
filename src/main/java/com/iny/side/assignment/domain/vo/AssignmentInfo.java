package com.iny.side.assignment.domain.vo;

import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.common.domain.GenderType;

import java.time.LocalDateTime;

public record AssignmentInfo(
        String title,
        String personaName,
        int personaAge,
        GenderType personaGender,
        String personaSymptom,
        String personaHistory,
        String personaPersonality,
        String personaDisease,
        String objective,
        int maxTurns,
        LocalDateTime dueDate
) {
    public static AssignmentInfo from(AssignmentCreateDto dto) {
        return new AssignmentInfo(
                dto.title(),
                dto.personaName(),
                dto.personaAge(),
                dto.personaGender(),
                dto.personaSymptom(),
                dto.personaHistory(),
                dto.personaPersonality(),
                dto.personaDisease(),
                dto.objective(),
                dto.maxTurns(),
                dto.dueDate()
        );
    }
}
