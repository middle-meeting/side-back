package com.iny.side.assignment.application.service;

import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;

import java.util.List;

public interface StudentAssignmentService {
    List<AssignmentSimpleResponseDto> getAll(Long courseId, Long accountId);
}
