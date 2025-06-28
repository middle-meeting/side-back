package com.iny.side.submission.web.dto;

import com.iny.side.submission.domain.entity.Prescription;

public record PrescriptionResponseDto(
    Long id,
    String drugName,
    String dosage,
    String frequency,
    String duration
) {
    
    public static PrescriptionResponseDto from(Prescription prescription) {
        return new PrescriptionResponseDto(
            prescription.getId(),
            prescription.getDrugName(),
            prescription.getDosage(),
            prescription.getFrequency(),
            prescription.getDuration()
        );
    }
}
