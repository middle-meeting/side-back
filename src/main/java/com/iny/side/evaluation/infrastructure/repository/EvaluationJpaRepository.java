package com.iny.side.evaluation.infrastructure.repository;

import com.iny.side.evaluation.domain.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationJpaRepository extends JpaRepository<Evaluation, Long> {
}
