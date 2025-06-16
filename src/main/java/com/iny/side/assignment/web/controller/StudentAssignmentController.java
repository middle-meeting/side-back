package com.iny.side.assignment.web.controller;

import com.iny.side.assignment.application.service.ProfessorAssignmentService;
import com.iny.side.assignment.application.service.StudentAssignmentService;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
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
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentAssignmentController {

    private final StudentAssignmentService studentAssignmentService;

    @GetMapping(value = "/courses/{courseId}/assignments")
    public ResponseEntity<BasicResponse<List<AssignmentSimpleResponseDto>>> getAll(
            @AuthenticationPrincipal AccountResponseDto student,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(BasicResponse.ok(studentAssignmentService.getAll(courseId, student.id())));
    }
}
