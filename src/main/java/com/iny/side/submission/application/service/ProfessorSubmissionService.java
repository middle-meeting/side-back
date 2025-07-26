package com.iny.side.submission.application.service;

import com.iny.side.submission.web.dto.SubmissionResponseDto;

public interface ProfessorSubmissionService {
    SubmissionResponseDto get(Long professorId, Long assignmentId, Long studentId);
}
