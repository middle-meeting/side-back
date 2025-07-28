package com.iny.side.evaluation.application.service;

import com.iny.side.evaluation.web.dto.SummaryResponseDto;

public interface StudentEvaluationService {
    SummaryResponseDto getMySummary(Long studentId, Long assignmentId);

}
