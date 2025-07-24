package com.iny.side.evaluation.infrastructure.repository;

import com.iny.side.evaluation.domain.entity.Evaluation;
import com.iny.side.evaluation.domain.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EvaluationRepositoryImpl implements EvaluationRepository {
    private final EvaluationJpaRepository evaluationJpaRepository;

    @Override
    public Evaluation save(Evaluation evaluation) {
        return evaluationJpaRepository.save(evaluation);
    }
}
