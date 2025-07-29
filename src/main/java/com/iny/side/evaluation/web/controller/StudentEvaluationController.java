package com.iny.side.evaluation.web.controller;

import com.iny.side.common.BasicResponse;
import com.iny.side.evaluation.application.service.StudentEvaluationService;
import com.iny.side.evaluation.web.dto.SummaryResponseDto;
import com.iny.side.users.web.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentEvaluationController {

    private final StudentEvaluationService studentEvaluationService;

    @GetMapping(value = "/assignments/{assignmentId}/my-summary")
    public ResponseEntity<BasicResponse<SummaryResponseDto>> getMySummary(
            @AuthenticationPrincipal AccountResponseDto student,
            @PathVariable Long assignmentId
    ) {
        return ResponseEntity.ok(BasicResponse.ok(studentEvaluationService.getMySummary(student.id(), assignmentId)));
    }
}
