package com.iny.side.evaluation.web.dto;

import com.iny.side.evaluation.domain.entity.Evaluation;

public record EvaluationResponseDto(
        long id,
        int score,
        String feedback,
        long accountId
) {

    public static EvaluationResponseDto from(Evaluation evaluation) {
        return new EvaluationResponseDto(
                evaluation.getId(),
                evaluation.getScore(),
                evaluation.getFeedback(),
                evaluation.getProfessor().getId()
        );
    }
}
