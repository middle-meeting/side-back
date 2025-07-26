package com.iny.side.submission.infrastructure.repository;

import com.iny.side.submission.domain.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {
    
    Optional<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);

    Optional<Submission> findByAssignmentId(Long assignmentId);
}
