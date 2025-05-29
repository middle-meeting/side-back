package com.iny.side.assignment.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.assignment.web.dto.AssignmentDto;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.CourseRepository;
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
    public List<AssignmentDto> findAssignmentsByCourseAndProfessor(Long courseId, Long professorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course", courseId));
        if (!course.getAccount().getId().equals(professorId)) {
            throw new ForbiddenException("본인의 강의가 아닙니다.");
        }

        return assignmentRepository.findByCourseId(courseId).stream()
                .map(AssignmentDto::from)
                .toList();
    }
}
