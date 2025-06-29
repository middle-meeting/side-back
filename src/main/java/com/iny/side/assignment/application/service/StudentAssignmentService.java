package com.iny.side.assignment.application.service;

import com.iny.side.assignment.web.dto.StudentAssignmentDetailResponseDto;
import com.iny.side.assignment.web.dto.StudentAssignmentSimpleResponseDto;
import com.iny.side.common.SliceResponse;

public interface StudentAssignmentService {

    SliceResponse<StudentAssignmentSimpleResponseDto> getAll(Long courseId, Long accountId, int page);

    StudentAssignmentDetailResponseDto get(Long courseId, Long studentId, Long assignmentId);
}
