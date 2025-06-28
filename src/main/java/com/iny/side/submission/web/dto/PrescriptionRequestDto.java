package com.iny.side.submission.web.dto;

import jakarta.validation.constraints.NotBlank;

public record PrescriptionRequestDto(
    @NotBlank(message = "약물명은 필수입니다")
    String drugName,
    
    @NotBlank(message = "용량은 필수입니다")
    String dosage,
    
    @NotBlank(message = "복용 빈도는 필수입니다")
    String frequency,
    
    @NotBlank(message = "복용 기간은 필수입니다")
    String duration
) {
}
