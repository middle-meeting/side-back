package com.iny.side.evaluation.web.controller;

import com.iny.side.common.BasicResponse;
import com.iny.side.evaluation.application.service.ProfessorEvaluationService;
import com.iny.side.evaluation.web.dto.ChatEvaluationResponseDto;
import com.iny.side.evaluation.web.dto.ChatsEvaluationRequestDto;
import com.iny.side.evaluation.web.dto.EvaluationRequestDto;
import com.iny.side.evaluation.web.dto.EvaluationResponseDto;
import com.iny.side.users.web.dto.AccountResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class ProfessorEvaluationController {

    private final ProfessorEvaluationService professorEvaluationService;

    @PostMapping(value = "/courses/{courseId}/assignments/{assignmentId}/students/{studentId}/evaluations")
    public ResponseEntity<BasicResponse<EvaluationResponseDto>> evaluate(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long courseId,
            @PathVariable Long assignmentId,
            @PathVariable Long studentId,
            @RequestBody @Valid EvaluationRequestDto evaluationRequestDto) {

        return ResponseEntity.ok(BasicResponse.ok(professorEvaluationService.evaluate(professor.id(), courseId, assignmentId, studentId, evaluationRequestDto)));
    }

    @PostMapping(value = "/courses/{courseId}/assignments/{assignmentId}/students/{studentId}/evaluations/chats")
    public ResponseEntity<BasicResponse<List<ChatEvaluationResponseDto>>> evaluateChats(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long courseId,
            @PathVariable Long assignmentId,
            @PathVariable Long studentId,
            @RequestBody @Valid ChatsEvaluationRequestDto evaluationRequestDto) {

        return ResponseEntity.ok(BasicResponse.ok(professorEvaluationService.evaluateChats(professor.id(), courseId, assignmentId, studentId, evaluationRequestDto)));
    }
}
