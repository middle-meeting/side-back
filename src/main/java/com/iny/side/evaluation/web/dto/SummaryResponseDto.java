package com.iny.side.evaluation.web.dto;

import com.iny.side.evaluation.domain.entity.Evaluation;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.web.dto.PrescriptionResponseDto;

import java.util.List;

public record SummaryResponseDto(
        int score,
        String feedback,
        String primaryDiagnosis,
        String subDiagnosis,
        List<PrescriptionResponseDto> prescriptions,
        String finalJudgment
) {

    public static SummaryResponseDto from(Submission submission, Evaluation eval, List<PrescriptionResponseDto> prescriptionDtos) {
        return new SummaryResponseDto(
                eval.getScore(),
                eval.getFeedback(),
                submission.getPrimaryDiagnosis(),
                submission.getSubDiagnosis(),
                prescriptionDtos,
                submission.getFinalJudgment()
        );
    }
}
