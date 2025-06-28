package com.iny.side.submission.infrastructure.repository;

import com.iny.side.submission.domain.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionJpaRepository extends JpaRepository<Prescription, Long> {
    
    List<Prescription> findBySubmissionId(Long submissionId);
    
    void deleteBySubmissionId(Long submissionId);
}
