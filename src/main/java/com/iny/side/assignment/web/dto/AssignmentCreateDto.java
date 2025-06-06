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
    /*
     * DTO에서 엔티티 생성을 담당하던 메서드는 Assignment.create()를 사용하도록
     * 리팩터링되면서 더 이상 사용되지 않는다. 도메인 로직 중복을 방지하기 위해
     * 해당 메서드는 제거한다.
     */
}
