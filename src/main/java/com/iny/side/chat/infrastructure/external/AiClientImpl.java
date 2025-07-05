package com.iny.side.chat.infrastructure.external;

import com.iny.side.chat.application.port.AiClient;
import com.iny.side.chat.infrastructure.external.dto.AiChatRequestDto;
import com.iny.side.chat.infrastructure.external.dto.AiChatResponseDto;
import com.iny.side.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiClientImpl implements AiClient {

    private final RestTemplate restTemplate;

    @Value("${ai.server.url:http://localhost:8000}")
    private String aiServerUrl;

    @Override
    public Result<AiChatResponseDto> sendChatMessage(AiChatRequestDto requestDto) {
//        if (true) {
//            String url = aiServerUrl + "/chat";
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            HttpEntity<AiChatRequestDto> request = new HttpEntity<>(requestDto, headers);
//            log.info("AI 서버 호출 - 요청: {}", request);
//            log.info("AI 서버 호출 성공 - 응답: {}", "TEST 응답입니다. 실제로는 AI 서버에서 생성된 응답이어야 합니다.");
//            AiChatResponseDto responseBody = new AiChatResponseDto("TEST 응답입니다. 실제로는 AI 서버에서 생성된 응답이어야 합니다.");
//            return Result.success(responseBody);
//        }
        try {
            String url = aiServerUrl + "/chat";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AiChatRequestDto> request = new HttpEntity<>(requestDto, headers);

            ResponseEntity<AiChatResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    AiChatResponseDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AiChatResponseDto responseBody = response.getBody();
                log.info("AI 서버 호출 성공 - 응답: {}", responseBody.answer());
                return Result.success(responseBody);
            } else {
                log.error("AI 서버 응답 오류 - 상태코드: {}", response.getStatusCode());
                return Result.failure("AI 서버 응답 오류", null);
            }

        } catch (Exception e) {
            log.error("AI 서버 호출 실패 - 오류: {}", e.getMessage(), e);
            return Result.failure("AI 서버 호출 실패: " + e.getMessage(), e);
        }
    }
}

