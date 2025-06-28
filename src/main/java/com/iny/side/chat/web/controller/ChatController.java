package com.iny.side.chat.web.controller;

import com.iny.side.chat.application.service.ChatService;
import com.iny.side.chat.web.dto.ChatMessageRequestDto;
import com.iny.side.chat.web.dto.ChatMessageResponseDto;
import com.iny.side.chat.web.dto.ChatResponseDto;
import com.iny.side.common.BasicResponse;
import com.iny.side.users.web.dto.AccountResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/assignments")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/{assignmentId}/chat/messages")
    public ResponseEntity<BasicResponse<ChatResponseDto>> sendMessage(
            @AuthenticationPrincipal AccountResponseDto student,
            @PathVariable Long assignmentId,
            @RequestBody @Valid ChatMessageRequestDto requestDto) {

        ChatResponseDto response = chatService.sendMessage(student.id(), assignmentId, requestDto.message());
        return ResponseEntity.ok(BasicResponse.ok(response));
    }

    @GetMapping("/{assignmentId}/chat/messages")
    public ResponseEntity<BasicResponse<List<ChatMessageResponseDto>>> getMessages(
            @AuthenticationPrincipal AccountResponseDto student,
            @PathVariable Long assignmentId) {

        List<ChatMessageResponseDto> messages = chatService.getMessages(student.id(), assignmentId);
        return ResponseEntity.ok(BasicResponse.ok(messages));
    }
}
