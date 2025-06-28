package com.iny.side.submission.web.controller;

import com.iny.side.common.BasicResponse;
import com.iny.side.submission.application.service.SubmissionService;
import com.iny.side.submission.web.dto.SubmissionRequestDto;
import com.iny.side.submission.web.dto.SubmissionResponseDto;
import com.iny.side.users.web.dto.AccountResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/assignments")
@RequiredArgsConstructor
public class  SubmissionController {

    private final SubmissionService submissionService;

    @PutMapping("/{assignmentId}/submission")
    public ResponseEntity<BasicResponse<SubmissionResponseDto>> submit(
            @AuthenticationPrincipal AccountResponseDto student,
            @PathVariable Long assignmentId,
            @RequestBody @Valid SubmissionRequestDto requestDto) {

        SubmissionResponseDto response = submissionService.submit(student.id(), assignmentId, requestDto);
        return ResponseEntity.ok(BasicResponse.ok(response));
    }
}
