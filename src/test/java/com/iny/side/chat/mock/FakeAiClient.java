package com.iny.side.chat.mock;

import com.iny.side.chat.application.port.AiClient;
import com.iny.side.chat.infrastructure.external.dto.AiChatRequestDto;
import com.iny.side.chat.infrastructure.external.dto.AiChatResponseDto;
import com.iny.side.chat.infrastructure.external.dto.ChatMessageSimpleDto;
import com.iny.side.common.result.Result;

public class FakeAiClient implements AiClient {

    private String mockResponse = "안녕하세요, 저는 환자입니다. 어떻게 도와드릴까요?";
    private boolean shouldFail = false;

    @Override
    public Result<AiChatResponseDto> sendChatMessage(AiChatRequestDto requestDto) {
        if (shouldFail) {
            return Result.failure("AI 서버 호출 실패", new RuntimeException("Mock failure"));
        }

        // 최근 메시지 가져오기 (학생의 마지막 메시지)
        String lastStudentMessage = getLastStudentMessage(requestDto.messages());

        // 간단한 응답 생성 로직
        String responseMessage;
        if (lastStudentMessage.contains("안녕") || lastStudentMessage.contains("hello")) {
            responseMessage = "안녕하세요! 저는 " + requestDto.personaName() + "입니다. 무엇을 도와드릴까요?";
        } else if (lastStudentMessage.contains("증상") || lastStudentMessage.contains("아프")) {
            responseMessage = "네, 요즘 " + requestDto.personaSymptom() + " 때문에 힘들어요.";
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

    private String getLastStudentMessage(java.util.List<ChatMessageSimpleDto> messages) {
        if (messages == null || messages.isEmpty()) {
            return "";
        }

        // 마지막 학생 메시지 찾기 (역순으로 탐색)
        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessageSimpleDto message = messages.get(i);
            if ("STUDENT".equals(message.role())) {
                return message.content();
            }
        }

        return "";
    }
}
