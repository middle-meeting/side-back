package com.iny.side.course.domain.repository;

import com.iny.side.course.domain.entity.Enrollment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {

    List<Enrollment> findAllByAccountIdAndSemester(Long accountId, String semester);

    Slice<Enrollment> findAllByAccountIdAndSemester(Long accountId, String semester, Pageable pageable);

    Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);

    Long countAllByCourseId(Long courseId);
}
