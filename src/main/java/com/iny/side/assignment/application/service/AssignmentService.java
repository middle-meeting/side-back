package com.iny.side.assignment.application.service;

import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentResponseDto;

import java.util.List;

public interface AssignmentService {
    List<AssignmentResponseDto> findAssignmentsByCourseAndProfessor(Long courseId, Long accountId);

    AssignmentResponseDto create(Long courseId, Long id, AssignmentCreateDto assignmentCreateDto);
}
