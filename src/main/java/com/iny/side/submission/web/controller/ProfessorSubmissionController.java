package com.iny.side.submission.web.controller;

import com.iny.side.common.BasicResponse;
import com.iny.side.submission.application.service.ProfessorSubmissionService;
import com.iny.side.submission.web.dto.ProfessorSubmissionResponseDto;
import com.iny.side.users.web.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/professor/assignments")
@RequiredArgsConstructor
public class ProfessorSubmissionController {

    private final ProfessorSubmissionService professorSubmissionService;

    @GetMapping("/{assignmentId}/students/{studentId}/submissions")
    public ResponseEntity<BasicResponse<ProfessorSubmissionResponseDto>> get(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long assignmentId,
            @PathVariable Long studentId
    ) {

        return ResponseEntity.ok(BasicResponse.ok(professorSubmissionService.get(professor.id(), assignmentId, studentId)));
    }
}
