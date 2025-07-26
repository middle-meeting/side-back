package com.iny.side.evaluation.web.dto;

import java.util.List;

public record ChatsEvaluationRequestDto(
        List<ChatEvaluationRequestDto> chatEvaluationRequestDtoList
) {
}
