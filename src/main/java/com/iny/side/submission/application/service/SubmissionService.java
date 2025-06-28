package com.iny.side.submission.application.service;

import com.iny.side.submission.web.dto.SubmissionRequestDto;
import com.iny.side.submission.web.dto.SubmissionResponseDto;

public interface SubmissionService {
    
    SubmissionResponseDto submit(Long studentId, Long assignmentId, SubmissionRequestDto requestDto);
}
