package com.iny.side.submission.infrastructure.repository;

import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import com.iny.side.submission.domain.vo.SubmissionDetailVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @Override
    public Long countEvaluatedByCourseIdAndAssignmentId(Long courseId, Long assignmentId) {
        return submissionJpaRepository.countEvaluatedByCourseIdAndAssignmentId(courseId, assignmentId);
    }

    @Override
    public Long countNotEvaluatedByCourseIdAndAssignmentId(Long courseId, Long assignmentId) {
        return submissionJpaRepository.countNotEvaluatedByCourseIdAndAssignmentId(courseId, assignmentId);
    }

    @Override
    public Long countNotSubmittedByCourseIdAndAssignmentId(Long courseId, Long assignmentId) {
        return submissionJpaRepository.countNotSubmittedByCourseIdAndAssignmentId(courseId, assignmentId);
    }

    @Override
    public Slice<SubmissionDetailVo> findAllByCourseIdAndAssignmentId(Long courseId, Long assignmentId, Pageable pageable) {
        return submissionJpaRepository.findAllByCourseIdAndAssignmentId(courseId, assignmentId, pageable);
    }

    @Override
    public Slice<SubmissionDetailVo> findEvaluatedByCourseIdAndAssignmentId(Long courseId, Long assignmentId, Pageable pageable) {
        return submissionJpaRepository.findEvaluatedByCourseIdAndAssignmentId(courseId, assignmentId, pageable);
    }

    @Override
    public Slice<SubmissionDetailVo> findEvaluationRequiredByCourseIdAndAssignmentId(Long courseId, Long assignmentId, Pageable pageable) {
        return submissionJpaRepository.findEvaluationRequiredByCourseIdAndAssignmentId(courseId, assignmentId, pageable);
    }

    @Override
    public Slice<SubmissionDetailVo> findNotSubmittedByCourseIdAndAssignmentId(Long courseId, Long assignmentId, Pageable pageable) {
        return submissionJpaRepository.findNotSubmittedByCourseIdAndAssignmentId(courseId, assignmentId, pageable);
    }
}
