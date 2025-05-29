package com.iny.side.assignment.infrastructure.repository;

import com.iny.side.assignment.domain.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentJpaRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findAllByCourseIdAndAccountId(Long course_id, Long account_id);

    Optional<Assignment> findByCourseId(Long courseId);
}
