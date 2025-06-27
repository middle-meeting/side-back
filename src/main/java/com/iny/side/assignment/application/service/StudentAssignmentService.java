package com.iny.side.assignment.application.service;

import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
import com.iny.side.assignment.web.dto.StudentAssignmentDetailResponseDto;
import com.iny.side.common.SliceResponse;

import java.util.List;

public interface StudentAssignmentService {
    List<AssignmentSimpleResponseDto> getAll(Long courseId, Long accountId);

    SliceResponse<AssignmentSimpleResponseDto> getAll(Long courseId, Long accountId, int page);

    StudentAssignmentDetailResponseDto get(Long courseId, Long studentId, Long assignmentId);
}
