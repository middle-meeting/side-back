package com.iny.side.submission.web.dto;

import com.iny.side.submission.domain.entity.Prescription;
import com.iny.side.submission.domain.vo.PrescriptionInfo;

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

    public static PrescriptionResponseDto from(PrescriptionInfo prescriptionInfo) {
        return new PrescriptionResponseDto(
                prescriptionInfo.id(),
                prescriptionInfo.drugName(),
                prescriptionInfo.dosage(),
                prescriptionInfo.frequency(),
                prescriptionInfo.duration()
        );
    }
}
