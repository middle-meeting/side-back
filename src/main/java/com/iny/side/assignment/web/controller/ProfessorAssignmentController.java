package com.iny.side.assignment.web.controller;

import com.iny.side.assignment.application.service.AssignmentService;
import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentDetailResponseDto;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
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
public class ProfessorAssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping(value = "/professor/courses/{courseId}/assignments")
    public ResponseEntity<BasicResponse<List<AssignmentSimpleResponseDto>>> getAll(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(BasicResponse.ok(assignmentService.getAll(courseId, professor.id())));
    }

    @PostMapping(value = "/professor/courses/{courseId}/assignments")
    public ResponseEntity<BasicResponse<AssignmentSimpleResponseDto>> create(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long courseId,
            @RequestBody AssignmentCreateDto createDto) {
        return ResponseEntity.ok(BasicResponse.ok(assignmentService.create(courseId, professor.id(), createDto)));
    }

    @GetMapping(value = "/professor/courses/{courseId}/assignments/{assignmentId}")
    public ResponseEntity<BasicResponse<AssignmentDetailResponseDto>> get(
            @AuthenticationPrincipal AccountResponseDto professor,
            @PathVariable Long courseId,
            @PathVariable Long assignmentId) {
        return ResponseEntity.ok(BasicResponse.ok(assignmentService.get(courseId, professor.id(), assignmentId)));
    }

    @DeleteMapping(value = "/professor/courses/{courseId}/assignments/{assignmentId}")
    public ResponseEntity<BasicResponse<Void>> delete(
            @AuthenticationPrincipal AccountResponseDto accountResponseDto,
            @PathVariable Long courseId,
            @PathVariable Long assignmentId) {
        assignmentService.delete(courseId, accountResponseDto.id(), assignmentId);
        return ResponseEntity.ok(BasicResponse.ok());
    }

}
