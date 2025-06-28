package com.iny.side.assignment.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
import com.iny.side.assignment.web.dto.StudentAssignmentDetailResponseDto;
import com.iny.side.common.SliceResponse;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.application.service.EnrollmentValidationService;
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
    public List<AssignmentSimpleResponseDto> getAll(Long courseId, Long studentId) {
        enrollmentValidationService.validateStudentEnrolledInCourse(courseId, studentId);
        return assignmentRepository.findAllByCourseId(courseId).stream()
                .map(AssignmentSimpleResponseDto::from)
                .toList();
    }

    @Override
    public SliceResponse<AssignmentSimpleResponseDto> getAll(Long courseId, Long studentId, int page) {
        enrollmentValidationService.validateStudentEnrolledInCourse(courseId, studentId);
        Pageable pageable = PageRequest.of(page, 12);
        Slice<Assignment> assignmentSlice = assignmentRepository.findAllByCourseId(courseId, pageable);

        List<AssignmentSimpleResponseDto> content = assignmentSlice.getContent().stream()
                .map(AssignmentSimpleResponseDto::from)
                .toList();

        return SliceResponse.of(content, page, 12, assignmentSlice.hasNext());
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
