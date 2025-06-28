package com.iny.side.submission.infrastructure.repository;

import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryImpl implements SubmissionRepository {
    
    private final SubmissionJpaRepository submissionJpaRepository;
    
    @Override
    public Submission save(Submission submission) {
        return submissionJpaRepository.save(submission);
    }
    
    @Override
    public Optional<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId) {
        return submissionJpaRepository.findByStudentIdAndAssignmentId(studentId, assignmentId);
    }
    
    @Override
    public Optional<Submission> findById(Long submissionId) {
        return submissionJpaRepository.findById(submissionId);
    }
}
