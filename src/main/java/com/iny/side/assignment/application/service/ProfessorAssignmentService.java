package com.iny.side.assignment.application.service;

import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
import com.iny.side.assignment.web.dto.ProfessorAssignmentDetailResponseDto;
import com.iny.side.common.SliceResponse;

import java.util.List;

public interface ProfessorAssignmentService {
    SliceResponse<AssignmentSimpleResponseDto> getAll(Long courseId, Long accountId, int page);

    AssignmentSimpleResponseDto create(Long courseId, Long id, AssignmentCreateDto assignmentCreateDto);

    ProfessorAssignmentDetailResponseDto get(Long courseId, Long accountId, Long assignmentId);

    void delete(Long courseId, Long accountId, Long assignmentId);
}
