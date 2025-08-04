package com.iny.side.submission.application.service;

import com.iny.side.common.SliceResponse;
import com.iny.side.submission.web.dto.ProfessorSubmissionDetailResponseDto;
import com.iny.side.submission.web.dto.SubmissionFilter;
import com.iny.side.submission.web.dto.SubmissionStatusResponseDto;
import com.iny.side.submission.web.dto.ProfessorSubmissionResponseDto;

public interface ProfessorSubmissionService {
    ProfessorSubmissionResponseDto get(Long professorId, Long assignmentId, Long studentId);

    SubmissionStatusResponseDto getStatus(Long accountId, Long courseId, Long assignmentId);

    SliceResponse<ProfessorSubmissionDetailResponseDto> getAll(Long accountId, Long courseId, Long assignmentId, SubmissionFilter status, int page);
}
