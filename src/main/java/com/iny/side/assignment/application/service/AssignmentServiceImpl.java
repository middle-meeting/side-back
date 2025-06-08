package com.iny.side.assignment.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.assignment.web.dto.AssignmentDetailResponseDto;
import com.iny.side.assignment.web.dto.AssignmentSimpleResponseDto;
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
    public List<AssignmentSimpleResponseDto> findAssignmentsByCourseAndProfessor(Long courseId, Long professorId) {
        validateProfessorOwnsCourse(courseId, professorId);

        return assignmentRepository.findAllByCourseId(courseId).stream()
                .map(AssignmentSimpleResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public AssignmentSimpleResponseDto create(Long courseId, Long professorId, AssignmentCreateDto assignmentCreateDto) {
        Course course = validateProfessorOwnsCourse(courseId, professorId);
        return AssignmentSimpleResponseDto.from(assignmentRepository.save(Assignment.create(course, assignmentCreateDto)));
    }

    @Override
    public AssignmentDetailResponseDto findAssignmentByCourseAndProfessor(Long courseId, Long professorId, Long assignmentId) {
        validateProfessorOwnsCourse(courseId, professorId);
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new NotFoundException("Assignment", assignmentId));
        validateAssignmentBelongsToCourse(courseId, assignment);
        return AssignmentDetailResponseDto.from(assignment);
    }


    @Override
    @Transactional
    public void deleteAssignmentByCourseAndProfessor(Long courseId, Long professorId, Long assignmentId) {
        validateProfessorOwnsCourse(courseId, professorId);
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new NotFoundException("Assignment", assignmentId));
        validateAssignmentBelongsToCourse(courseId, assignment);
        assignmentRepository.delete(assignment);
    }

    private static void validateAssignmentBelongsToCourse(Long courseId, Assignment assignment) {
        if (!assignment.getCourse().getId().equals(courseId)) {
            throw new ForbiddenException("해당 강의의 과제가 아닙니다.");
        }
    }

    private Course validateProfessorOwnsCourse(Long courseId, Long professorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course", courseId));
        course.validateOwner(professorId);
        return course;
    }
}
