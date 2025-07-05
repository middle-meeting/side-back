package com.iny.side.chat.infrastructure.external.dto;

import com.iny.side.common.domain.GenderType;

import java.util.List;

public record AiChatRequestDto(
        String personaName,
        Integer personaAge,
        GenderType personaGender,
        String personaSymptom,
        String personaHistory,
        String personaPersonality,
        String personaDisease,
        List<ChatMessageSimpleDto> messages
) {
    public static AiChatRequestDto of(String name, Integer age, GenderType gender, String symptom, String history,
                                      String personality, String Disease, List<ChatMessageSimpleDto> messages) {
        return new AiChatRequestDto(
                name,
                age,
                gender,
                symptom,
                history,
                personality,
                Disease,
                messages
        );
    }
}
