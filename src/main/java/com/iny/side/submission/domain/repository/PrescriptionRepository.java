package com.iny.side.submission.domain.repository;

import com.iny.side.submission.domain.entity.Prescription;

import java.util.List;

public interface PrescriptionRepository {
    
    Prescription save(Prescription prescription);
    
    List<Prescription> findBySubmissionId(Long submissionId);
    
    void deleteBySubmissionId(Long submissionId);
}
