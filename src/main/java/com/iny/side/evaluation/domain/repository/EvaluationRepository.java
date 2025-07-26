package com.iny.side.evaluation.domain.repository;

import com.iny.side.evaluation.domain.entity.Evaluation;

public interface EvaluationRepository {
    Evaluation save(Evaluation evaluation);
}
