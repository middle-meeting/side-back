package com.iny.side.course.web.controller;

import com.iny.side.common.BasicResponse;
import com.iny.side.common.SliceResponse;
import com.iny.side.course.application.service.CourseService;
import com.iny.side.course.web.dto.EnrolledCoursesDetailDto;
import com.iny.side.course.web.dto.EnrolledCoursesSimpleDto;
import com.iny.side.users.web.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnrolledCourseController {

    private final CourseService courseService;

    @GetMapping(value = "/student/courses")
    public ResponseEntity<BasicResponse<SliceResponse<EnrolledCoursesSimpleDto>>> getAll(
            @AuthenticationPrincipal AccountResponseDto accountResponseDto,
            @RequestParam(value = "semester") String semester,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        return ResponseEntity.ok(BasicResponse.ok(courseService.getAllEnrolled(accountResponseDto.id(), semester, page)));
    }

    @GetMapping(value = "/student/courses/{courseId}")
    public ResponseEntity<BasicResponse<EnrolledCoursesDetailDto>> getAll(
            @AuthenticationPrincipal AccountResponseDto accountResponseDto,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(BasicResponse.ok(courseService.getEnrolled(accountResponseDto.id(), courseId)));
    }
}
