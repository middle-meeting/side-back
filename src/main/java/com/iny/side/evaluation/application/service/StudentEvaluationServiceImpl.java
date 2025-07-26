package com.iny.side.evaluation.application.service;

import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.evaluation.domain.entity.Evaluation;
import com.iny.side.evaluation.domain.repository.EvaluationRepository;
import com.iny.side.evaluation.web.dto.SummaryResponseDto;
import com.iny.side.submission.domain.entity.Prescription;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.PrescriptionRepository;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import com.iny.side.submission.web.dto.PrescriptionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentEvaluationServiceImpl implements StudentEvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final SubmissionRepository submissionRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    @Transactional(readOnly = true)
    public SummaryResponseDto getMySummary(Long studentId, Long assignmentId) {
        // 1. 과제 수행 기록 조회
        Submission submission = submissionRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new NotFoundException("과제 수행 기록"));

        // 2. 해당 학생이 수행한 과제인지 확인
        if (!submission.getStudent().getId().equals(studentId)) {
            throw new ForbiddenException("forbidden.not_your_assignment");
        }

        // 3. 해당 과제 수행에 대한 교수 채점이 있는지 조회
        Evaluation eval = evaluationRepository.findBySubmissionId(submission.getId())
                .orElseThrow(() -> new NotFoundException("평가"));

        // 4. 처방전 조회
        List<Prescription> prescriptions = prescriptionRepository.findBySubmissionId(submission.getId());

        List<PrescriptionResponseDto> prescriptionDtos = prescriptions.stream()
                .map(PrescriptionResponseDto::from)
                .toList();

        return SummaryResponseDto.from(submission, eval, prescriptionDtos);
    }
}
