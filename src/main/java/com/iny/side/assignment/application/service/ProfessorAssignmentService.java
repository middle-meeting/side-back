package com.iny.side.assignment.application.service;

import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
import com.iny.side.assignment.web.dto.ProfessorAssignmentDetailResponseDto;

import java.util.List;

public interface ProfessorAssignmentService {
    List<AssignmentSimpleResponseDto> getAll(Long courseId, Long accountId);

    AssignmentSimpleResponseDto create(Long courseId, Long id, AssignmentCreateDto assignmentCreateDto);

    ProfessorAssignmentDetailResponseDto get(Long courseId, Long accountId, Long assignmentId);

    void delete(Long courseId, Long accountId, Long assignmentId);
}
