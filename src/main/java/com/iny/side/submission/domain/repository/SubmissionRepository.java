package com.iny.side.submission.domain.repository;

import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.vo.SubmissionDetailVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubmissionRepository {
    
    Submission save(Submission submission);
    
    Optional<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);
    
    Optional<Submission> findById(Long submissionId);

    Long countEvaluatedByCourseIdAndAssignmentId(Long courseId, Long assignmentId);

    Long countNotEvaluatedByCourseIdAndAssignmentId(Long courseId, Long assignmentId);

    Long countNotSubmittedByCourseIdAndAssignmentId(Long courseId, Long assignmentId);

    Slice<SubmissionDetailVo> findAllByCourseIdAndAssignmentId(@Param("courseId") Long courseId, @Param("assignmentId") Long assignmentId, Pageable pageable);

    Slice<SubmissionDetailVo> findEvaluatedByCourseIdAndAssignmentId(@Param("courseId") Long courseId, @Param("assignmentId") Long assignmentId, Pageable pageable);

    Slice<SubmissionDetailVo> findEvaluationRequiredByCourseIdAndAssignmentId(@Param("courseId") Long courseId, @Param("assignmentId") Long assignmentId, Pageable pageable);

    Slice<SubmissionDetailVo> findNotSubmittedByCourseIdAndAssignmentId(@Param("courseId") Long courseId, @Param("assignmentId") Long assignmentId, Pageable pageable);
}
