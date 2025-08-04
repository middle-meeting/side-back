package com.iny.side.submission.domain.vo;

import com.iny.side.submission.domain.entity.Prescription;
import com.iny.side.submission.web.dto.PrescriptionResponseDto;

public record PrescriptionInfo(
        Long id,
        Long submissionId,
        String drugName,
        String dosage,
        String frequency,
        String duration
) {

    public static PrescriptionInfo from(Prescription prescription) {
        return new PrescriptionInfo(
                prescription.getId(),
                prescription.getSubmission().getId(),
                prescription.getDrugName(),
                prescription.getDosage(),
                prescription.getFrequency(),
                prescription.getDuration()
        );
    }
}
