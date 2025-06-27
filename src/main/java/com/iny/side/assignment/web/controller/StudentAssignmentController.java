package com.iny.side.assignment.web.controller;

import com.iny.side.assignment.application.service.StudentAssignmentService;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
import com.iny.side.assignment.web.dto.StudentAssignmentDetailResponseDto;
import com.iny.side.common.BasicResponse;
import com.iny.side.common.SliceResponse;
import com.iny.side.users.web.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentAssignmentController {

    private final StudentAssignmentService studentAssignmentService;

    @GetMapping(value = "/courses/{courseId}/assignments")
    public ResponseEntity<BasicResponse<SliceResponse<AssignmentSimpleResponseDto>>> getAll(
            @AuthenticationPrincipal AccountResponseDto student,
            @PathVariable Long courseId,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        return ResponseEntity.ok(BasicResponse.ok(studentAssignmentService.getAll(courseId, student.id(), page)));
    }

    @GetMapping(value = "/courses/{courseId}/assignments/{assignmentId}")
    public ResponseEntity<BasicResponse<StudentAssignmentDetailResponseDto>> get(
            @AuthenticationPrincipal AccountResponseDto student,
            @PathVariable Long courseId,
            @PathVariable Long assignmentId) {
        return ResponseEntity.ok(BasicResponse.ok(studentAssignmentService.get(courseId, student.id(), assignmentId)));
    }
}
