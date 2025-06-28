package com.iny.side.submission.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SubmissionRequestDto(
    @NotBlank(message = "주진단은 필수입니다")
    String primaryDiagnosis,
    
    String subDiagnosis,
    
    @NotEmpty(message = "처방은 최소 1개 이상 필요합니다")
    @Valid
    List<PrescriptionRequestDto> prescriptions,
    
    @NotBlank(message = "임상적 근거 및 판단 과정은 필수입니다")
    String finalJudgment
) {
}
