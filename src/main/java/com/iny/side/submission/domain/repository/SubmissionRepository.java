package com.iny.side.submission.domain.repository;

import com.iny.side.submission.domain.entity.Submission;

import java.util.Optional;

public interface SubmissionRepository {
    
    Submission save(Submission submission);
    
    Optional<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);
    
    Optional<Submission> findById(Long submissionId);

    Optional<Submission> findByAssignmentId(Long assignmentId);
}
