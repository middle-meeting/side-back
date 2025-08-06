package com.iny.side.evaluation.infrastructure.repository;

import com.iny.side.evaluation.domain.entity.Evaluation;
import com.iny.side.evaluation.domain.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EvaluationRepositoryImpl implements EvaluationRepository {
    private final EvaluationJpaRepository evaluationJpaRepository;

    @Override
    public Evaluation save(Evaluation evaluation) {
        return evaluationJpaRepository.save(evaluation);
    }

    @Override
    public Optional<Evaluation> findBySubmissionIdAndAccountId(Long submissionId, Long accountId) {
        return evaluationJpaRepository.findBySubmissionIdAndProfessorId(submissionId, accountId);
    }
  
    @Override
    public Optional<Evaluation> findBySubmissionId(Long submissionId) {
        return evaluationJpaRepository.findBySubmissionId(submissionId);
    }
}
