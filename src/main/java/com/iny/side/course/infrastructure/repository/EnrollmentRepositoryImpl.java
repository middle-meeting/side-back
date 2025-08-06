package com.iny.side.course.infrastructure.repository;

import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    private final EnrollmentJpaRepository enrollmentJpaRepository;

    @Override
    public List<Enrollment> findAllByAccountIdAndSemester(Long accountId, String semester) {
        return enrollmentJpaRepository.findAllByAccountIdAndSemester(accountId, semester);
    }

    @Override
    public Slice<Enrollment> findAllByAccountIdAndSemester(Long accountId, String semester, Pageable pageable) {
        return enrollmentJpaRepository.findAllByAccountIdAndSemester(accountId, semester, pageable);
    }

    @Override
    public Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId) {
        return enrollmentJpaRepository.findByCourseIdAndAccountId(courseId, studentId);
    }

    @Override
    public Long countAllByCourseId(Long courseId) {
        return enrollmentJpaRepository.countAllByCourseId(courseId);
    }
}
