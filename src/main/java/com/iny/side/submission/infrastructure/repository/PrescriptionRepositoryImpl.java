package com.iny.side.submission.infrastructure.repository;

import com.iny.side.submission.domain.entity.Prescription;
import com.iny.side.submission.domain.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PrescriptionRepositoryImpl implements PrescriptionRepository {
    
    private final PrescriptionJpaRepository prescriptionJpaRepository;
    
    @Override
    public Prescription save(Prescription prescription) {
        return prescriptionJpaRepository.save(prescription);
    }
    
    @Override
    public List<Prescription> findBySubmissionId(Long submissionId) {
        return prescriptionJpaRepository.findBySubmissionId(submissionId);
    }
    
    @Override
    public void deleteBySubmissionId(Long submissionId) {
        prescriptionJpaRepository.deleteBySubmissionId(submissionId);
    }

    @Override
    public List<Prescription> findBySubmissionIdIn(List<Long> submissionIds) {
        return prescriptionJpaRepository.findBySubmissionIdIn(submissionIds);
    }
}
