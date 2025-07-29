package com.iny.side.evaluation.application.service;

import com.iny.side.evaluation.web.dto.ChatEvaluationResponseDto;
import com.iny.side.evaluation.web.dto.ChatsEvaluationRequestDto;
import com.iny.side.evaluation.web.dto.EvaluationRequestDto;
import com.iny.side.evaluation.web.dto.EvaluationResponseDto;

import java.util.List;

public interface ProfessorEvaluationService {
    EvaluationResponseDto get(Long accountId, Long courseId, Long assignmentId, Long studentId);

    EvaluationResponseDto evaluate(Long accountId, Long courseId, Long assignmentId, Long studentId, EvaluationRequestDto evaluationRequestDto);

    List<ChatEvaluationResponseDto> evaluateChats(Long accountId, Long courseId, Long assignmentId, Long studentId, ChatsEvaluationRequestDto evaluationRequestDto);
}
