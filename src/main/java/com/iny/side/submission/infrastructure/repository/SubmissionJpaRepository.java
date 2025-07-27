package com.iny.side.submission.infrastructure.repository;

import com.iny.side.submission.domain.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {

    Optional<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);
}
