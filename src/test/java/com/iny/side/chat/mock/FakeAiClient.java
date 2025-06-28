package com.iny.side.chat.mock;

import com.iny.side.chat.infrastructure.external.AiClient;
import com.iny.side.chat.infrastructure.external.dto.AiChatRequestDto;
import com.iny.side.chat.infrastructure.external.dto.AiChatResponseDto;
import com.iny.side.common.result.Result;

public class FakeAiClient implements AiClient {
    
    private String mockResponse = "안녕하세요, 저는 환자입니다. 어떻게 도와드릴까요?";
    private boolean shouldFail = false;
    
    @Override
    public Result<AiChatResponseDto> sendChatMessage(AiChatRequestDto requestDto) {
        if (shouldFail) {
            return Result.failure("AI 서버 호출 실패", new RuntimeException("Mock failure"));
        }

        String message = requestDto.message();
        String patientContext = requestDto.patientContext();

        // 간단한 응답 생성 로직
        String responseMessage;
        if (message.contains("안녕") || message.contains("hello")) {
            responseMessage = "안녕하세요! 저는 " + extractPatientName(patientContext) + "입니다. 무엇을 도와드릴까요?";
        } else if (message.contains("증상") || message.contains("아프")) {
            responseMessage = "네, 요즘 " + extractSymptom(patientContext) + " 때문에 힘들어요.";
        } else {
            responseMessage = mockResponse;
        }

        return Result.success(new AiChatResponseDto(responseMessage));
    }
    
    public void setMockResponse(String mockResponse) {
        this.mockResponse = mockResponse;
    }
    
    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }
    
    private String extractPatientName(String patientContext) {
        if (patientContext.contains("이름=")) {
            int start = patientContext.indexOf("이름=") + 3;
            int end = patientContext.indexOf(",", start);
            if (end == -1) end = patientContext.length();
            return patientContext.substring(start, end);
        }
        return "환자";
    }
    
    private String extractSymptom(String patientContext) {
        if (patientContext.contains("증상=")) {
            int start = patientContext.indexOf("증상=") + 3;
            int end = patientContext.indexOf(",", start);
            if (end == -1) end = patientContext.length();
            return patientContext.substring(start, end);
        }
        return "몸이 안 좋은 것";
    }
}
