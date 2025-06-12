package com.iny.side.course.infrastructure.repository;

import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    private final EnrollmentJpaRepository enrollmentJpaRepository;

    @Override
    public List<Enrollment> findAllByAccountIdAndSemester(Long accountId, String semester) {
        return enrollmentJpaRepository.findAllByAccountIdAndSemester(accountId, semester);
    }

}
