package com.iny.side.assignment.web.dto;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.common.domain.GenderType;
import com.iny.side.course.domain.entity.Course;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record AssignmentCreateDto(String title, String personaName, int personaAge, GenderType personaGender,
                                  String personaSymptom, String personaHistory, String personaPersonality,
                                  String personaDisease, String objective, int maxTurns,
                                  @Future(message = "마감일이 올바르지 않습니다.")
                                  LocalDateTime dueDate) {
}
