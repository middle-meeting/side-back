package com.iny.side.course.domain.repository;

import com.iny.side.course.domain.entity.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {

    List<Enrollment> findAllByAccountIdAndSemester(Long accountId, String semester);

    Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);

}
