package com.iny.side.evaluation.domain.repository;

import com.iny.side.evaluation.domain.entity.Evaluation;

import java.util.Optional;

public interface EvaluationRepository {
    Evaluation save(Evaluation evaluation);

    Optional<Evaluation> findBySubmissionIdAndAccountId(Long submissionId, Long accountId);
}
