package com.iny.side.assignment.application.service;

import com.iny.side.assignment.web.dto.AssignmentDto;

import java.util.List;

public interface AssignmentService {
    List<AssignmentDto> findAssignmentsByCourseAndProfessor(Long courseId, Long accountId);
}
