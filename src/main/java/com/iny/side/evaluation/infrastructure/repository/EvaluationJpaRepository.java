package com.iny.side.evaluation.infrastructure.repository;

import com.iny.side.evaluation.domain.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationJpaRepository extends JpaRepository<Evaluation, Long> {

    Optional<Evaluation> findBySubmissionIdAndProfessorId(Long submissionId, Long accountId);
}
