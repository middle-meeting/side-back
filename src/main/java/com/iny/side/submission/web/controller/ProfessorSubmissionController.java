package com.iny.side.submission.web.controller;

import com.iny.side.common.BasicResponse;
import com.iny.side.common.SliceResponse;
import com.iny.side.submission.web.dto.ProfessorSubmissionDetailResponseDto;
import com.iny.side.submission.web.dto.SubmissionFilter;
import com.iny.side.submission.web.dto.SubmissionStatusResponseDto;
import com.iny.side.submission.application.service.ProfessorSubmissionService;
import com.iny.side.submission.web.dto.ProfessorSubmissionResponseDto;
import com.iny.side.users.web.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class ProfessorSubmissionController {

    private final ProfessorSubmissionService professorSubmissionService;

    @GetMapping("/assignment/{assignmentId}/students/{studentId}/submissions")
    public ResponseEntity<BasicResponse<ProfessorSubmissionResponseDto>> get(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long assignmentId,
            @PathVariable Long studentId
    ) {

        return ResponseEntity.ok(BasicResponse.ok(professorSubmissionService.get(professor.id(), assignmentId, studentId)));
    }

    @GetMapping(value = "/courses/{courseId}/assignments/{assignmentId}/submissions/status")
    public ResponseEntity<BasicResponse<SubmissionStatusResponseDto>> getStatus(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long courseId,
            @PathVariable Long assignmentId) {

        return ResponseEntity.ok(BasicResponse.ok(professorSubmissionService.getStatus(professor.id(), courseId, assignmentId)));
    }

    @GetMapping(value = "/courses/{courseId}/assignments/{assignmentId}/submissions")
    public ResponseEntity<BasicResponse<SliceResponse<ProfessorSubmissionDetailResponseDto>>> getAll(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long courseId,
            @PathVariable Long assignmentId,
            @RequestParam(defaultValue = "ALL") SubmissionFilter status,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        return ResponseEntity.ok(BasicResponse.ok(professorSubmissionService.getAll(professor.id(), courseId, assignmentId, status, page)));
    }
}
