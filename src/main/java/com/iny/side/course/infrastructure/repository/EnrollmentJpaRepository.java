package com.iny.side.course.infrastructure.repository;

import com.iny.side.course.domain.entity.Enrollment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentJpaRepository extends JpaRepository<Enrollment, Long> {
    @Query("SELECT e FROM Enrollment e WHERE e.account.id = :studentId AND e.course.semester = :semester")
    List<Enrollment> findAllByAccountIdAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);

    @Query("SELECT e FROM Enrollment e WHERE e.account.id = :studentId AND e.course.semester = :semester")
    Slice<Enrollment> findAllByAccountIdAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester, Pageable pageable);

    Optional<Enrollment> findByCourseIdAndAccountId(Long courseId, Long studentId);
}
