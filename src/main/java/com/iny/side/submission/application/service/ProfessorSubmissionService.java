package com.iny.side.submission.application.service;

import com.iny.side.submission.web.dto.ProfessorSubmissionResponseDto;

public interface ProfessorSubmissionService {
    ProfessorSubmissionResponseDto get(Long professorId, Long assignmentId, Long studentId);
}
