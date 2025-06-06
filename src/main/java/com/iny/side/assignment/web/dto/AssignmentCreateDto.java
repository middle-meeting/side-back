package com.iny.side.assignment.web.dto;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.common.domain.GenderType;
import com.iny.side.course.domain.entity.Course;

import java.time.LocalDateTime;

public record AssignmentCreateDto(String title, String personaName, int personaAge, GenderType personaGender,
                                  String personaSymptom, String personaHistory, String personaPersonality,
                                  String personaDisease, String objective, int maxTurns, LocalDateTime dueDate) {
    public Assignment to(Course course) {
        return Assignment.builder()
                .title(title)
                .personaName(personaName)
                .personaAge(personaAge)
                .personaGender(personaGender)
                .personaSymptom(personaSymptom)
                .personaHistory(personaHistory)
                .personaPersonality(personaPersonality)
                .personaDisease(personaDisease)
                .objective(objective)
                .maxTurns(maxTurns)
                .dueDate(dueDate)
                .account(course.getAccount())
                .course(course)
                .build();
    }
}
