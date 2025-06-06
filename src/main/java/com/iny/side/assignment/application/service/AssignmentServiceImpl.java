package com.iny.side.assignment.application.service;

import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentResponseDto;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    @Override
    public List<AssignmentResponseDto> findAssignmentsByCourseAndProfessor(Long courseId, Long professorId) {
        validateProfessorOwnsCourse(courseId, professorId);

        return assignmentRepository.findByCourseId(courseId).stream()
                .map(AssignmentResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public AssignmentResponseDto create(Long courseId, Long professorId, AssignmentCreateDto assignmentCreateDto) {
        Course course = validateProfessorOwnsCourse(courseId, professorId);
        return AssignmentResponseDto.from(assignmentRepository.save(assignmentCreateDto.to(course)));
    }

    private Course validateProfessorOwnsCourse(Long courseId, Long professorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course", courseId));
        if (!course.getAccount().getId().equals(professorId)) {
            throw new ForbiddenException("본인의 강의가 아닙니다.");
        }
        return course;
    }
}
