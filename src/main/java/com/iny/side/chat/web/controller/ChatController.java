package com.iny.side.chat.web.controller;

import com.iny.side.chat.application.service.ChatService;
import com.iny.side.chat.web.dto.ChatMessageRequestDto;
import com.iny.side.chat.web.dto.ChatResponseDto;
import com.iny.side.common.BasicResponse;
import com.iny.side.users.web.dto.AccountResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student/chat")
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatService chatService;
    
    @PostMapping("/message")
    public ResponseEntity<BasicResponse<ChatResponseDto>> sendMessage(
            @AuthenticationPrincipal AccountResponseDto student,
            @RequestBody @Valid ChatMessageRequestDto requestDto) {
        
        ChatResponseDto response = chatService.sendMessage(student.id(), requestDto);
        return ResponseEntity.ok(BasicResponse.ok(response));
    }
}
