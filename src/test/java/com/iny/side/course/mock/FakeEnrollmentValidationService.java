package com.iny.side.course.mock;

import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.application.service.EnrollmentValidationService;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.CourseRepository;
import com.iny.side.course.domain.repository.EnrollmentRepository;

public class FakeEnrollmentValidationService extends EnrollmentValidationService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    
    public FakeEnrollmentValidationService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository) {
        super(enrollmentRepository, courseRepository);
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }
    
    @Override
    public void validateStudentEnrolledInCourse(Long courseId, Long studentId) {
        enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new ForbiddenException("forbidden.not_enrolled"));
    }
    
    @Override
    public Course validateProfessorOwnsCourse(Long courseId, Long professorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course"));
        course.validateOwner(professorId);
        return course;
    }
}
