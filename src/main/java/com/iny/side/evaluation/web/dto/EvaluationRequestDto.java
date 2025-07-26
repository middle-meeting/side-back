package com.iny.side.evaluation.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record EvaluationRequestDto(
        @Min(value = 0, message = "최종점수는 0점 이상이어야 합니다.")
        @Max(value = 100, message = "최종점수는 100점 이하여야 합니다.")
        int score,
        String feedback) {
}
