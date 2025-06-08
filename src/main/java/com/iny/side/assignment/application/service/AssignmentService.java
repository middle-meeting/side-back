package com.iny.side.assignment.application.service;

import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentDetailResponseDto;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;

import java.util.List;

public interface AssignmentService {
    List<AssignmentSimpleResponseDto> findAssignmentsByCourseAndProfessor(Long courseId, Long accountId);

    AssignmentSimpleResponseDto create(Long courseId, Long id, AssignmentCreateDto assignmentCreateDto);

    AssignmentDetailResponseDto findAssignmentByCourseAndProfessor(Long courseId, Long accountId, Long assignmentId);

    void deleteAssignmentByCourseAndProfessor(Long courseId, Long accountId, Long assignmentId);
}
