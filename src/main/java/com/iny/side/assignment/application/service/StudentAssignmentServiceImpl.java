package com.iny.side.assignment.application.service;

import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class StudentAssignmentServiceImpl implements StudentAssignmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    public List<AssignmentSimpleResponseDto> getAll(Long courseId, Long studentId) {
        validateStudentEnrolledCourse(courseId, studentId);
        return assignmentRepository.findAllByCourseId(courseId).stream()
                .map(AssignmentSimpleResponseDto::from)
                .toList();
    }

    private void validateStudentEnrolledCourse(Long courseId, Long studentId) {
        enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new ForbiddenException("해당 강의를 수강하지 않습니다."));
    }
}
