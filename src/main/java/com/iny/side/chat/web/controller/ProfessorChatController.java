package com.iny.side.chat.web.controller;

import com.iny.side.chat.application.service.ProfessorChatService;
import com.iny.side.chat.web.dto.ProfessorChatMessageResponseDto;
import com.iny.side.common.BasicResponse;
import com.iny.side.users.web.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/professor/assignments")
@RequiredArgsConstructor
public class ProfessorChatController {

    private final ProfessorChatService professorChatService;

    @GetMapping("/{assignmentId}/students/{studentId}/chats")
    public ResponseEntity<BasicResponse<List<ProfessorChatMessageResponseDto>>> getMessages(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long assignmentId,
            @PathVariable Long studentId
            ) {

        List<ProfessorChatMessageResponseDto> messages = professorChatService.getMessages(professor.id(), assignmentId, studentId);
        return ResponseEntity.ok(BasicResponse.ok(messages));
    }
}
