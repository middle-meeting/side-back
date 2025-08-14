package com.iny.side.assignment.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
import com.iny.side.assignment.web.dto.StudentAssignmentDetailResponseDto;
import com.iny.side.assignment.web.dto.StudentAssignmentSimpleResponseDto;
import com.iny.side.common.SliceResponse;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.application.service.EnrollmentValidationService;
import com.iny.side.submission.domain.entity.Submission;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class StudentAssignmentServiceImpl implements StudentAssignmentService {

    private final EnrollmentValidationService enrollmentValidationService;
    private final AssignmentRepository assignmentRepository;

    @Override
    public SliceResponse<StudentAssignmentSimpleResponseDto> getAll(Long courseId, Long studentId, int page) {
        // 1. 권한 검증
        enrollmentValidationService.validateStudentEnrolledInCourse(courseId, studentId);
        // 2. 페이징 정보 생성
        Pageable pageable = PageRequest.of(page, 12);
        // 3. repository 에서 과제의 상태가 결정된 상태로 넘어옴
        Slice<StudentAssignmentSimpleResponseDto> assignmentSlice = assignmentRepository.findAllByCourseIdAndStudentId(courseId, studentId, pageable);

        return SliceResponse.from(assignmentSlice);
    }

    @Override
    public StudentAssignmentDetailResponseDto get(Long courseId, Long studentId, Long assignmentId) {
        enrollmentValidationService.validateStudentEnrolledInCourse(courseId, studentId);
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new NotFoundException("Assignment"));
        validateAssignmentBelongsToCourse(courseId, assignment);
        return StudentAssignmentDetailResponseDto.from(assignment);
    }

    private static void validateAssignmentBelongsToCourse(Long courseId, Assignment assignment) {
        if (!assignment.getCourse().getId().equals(courseId)) {
            throw new ForbiddenException("forbidden.assignment_not_in_course");
        }
    }
}
