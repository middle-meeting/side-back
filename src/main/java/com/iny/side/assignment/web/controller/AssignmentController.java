package com.iny.side.assignment.web.controller;

import com.iny.side.assignment.application.service.AssignmentService;
import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentResponseDto;
import com.iny.side.common.BasicResponse;
import com.iny.side.users.web.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping(value = "/professor/courses/{courseId}/assignments")
    public ResponseEntity<BasicResponse<List<AssignmentResponseDto>>> getAssignments(
            @AuthenticationPrincipal AccountResponseDto accountResponseDto,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(BasicResponse.ok(assignmentService.findAssignmentsByCourseAndProfessor(courseId, accountResponseDto.id())));
    }

    @PostMapping(value = "/professor/courses/{courseId}/assignments")
    public ResponseEntity<BasicResponse<AssignmentResponseDto>> create(
            @AuthenticationPrincipal AccountResponseDto accountResponseDto,
            @PathVariable Long courseId,
            @RequestBody AssignmentCreateDto assignmentCreateDto) {

        return ResponseEntity.ok(BasicResponse.ok(assignmentService.create(courseId, accountResponseDto.id(), assignmentCreateDto)));
    }
}
