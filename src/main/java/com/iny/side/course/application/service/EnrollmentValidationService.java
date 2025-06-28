package com.iny.side.course.application.service;

import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.CourseRepository;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 수강 관련 검증 로직을 담당하는 공통 서비스
 */
@Service
@RequiredArgsConstructor
public class EnrollmentValidationService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    
    /**
     * 학생이 해당 강의에 수강 등록되어 있는지 검증
     * 
     * @param courseId 강의 ID
     * @param studentId 학생 ID
     * @throws ForbiddenException 수강 등록되지 않은 경우
     */
    public void validateStudentEnrolledInCourse(Long courseId, Long studentId) {
        enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new ForbiddenException("forbidden.not_enrolled"));
    }
    
    /**
     * 교수가 해당 강의의 소유자인지 검증하고 강의 정보 반환
     * 
     * @param courseId 강의 ID
     * @param professorId 교수 ID
     * @return 검증된 강의 정보
     * @throws NotFoundException 강의가 존재하지 않는 경우
     * @throws ForbiddenException 교수가 강의 소유자가 아닌 경우
     */
    public Course validateProfessorOwnsCourse(Long courseId, Long professorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course"));
        course.validateOwner(professorId);
        return course;
    }
}
