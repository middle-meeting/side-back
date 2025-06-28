package com.iny.side.submission.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.application.service.EnrollmentValidationService;
import com.iny.side.submission.domain.entity.Prescription;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.PrescriptionRepository;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import com.iny.side.submission.web.dto.PrescriptionRequestDto;
import com.iny.side.submission.web.dto.PrescriptionResponseDto;
import com.iny.side.submission.web.dto.SubmissionRequestDto;
import com.iny.side.submission.web.dto.SubmissionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AssignmentRepository assignmentRepository;
    private final EnrollmentValidationService enrollmentValidationService;
    
    @Override
    @Transactional
    public SubmissionResponseDto submit(Long studentId, Long assignmentId, SubmissionRequestDto requestDto) {
        // 1. 과제 조회
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new NotFoundException("과제"));

        // 2. 학생이 해당 과제의 수강생인지 검증
        enrollmentValidationService.validateStudentEnrolledInCourse(assignment.getCourse().getId(), studentId);

        // 3. Submission 조회
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElseThrow(() -> new NotFoundException("과제 수행 기록"));

        // 4. 기존 처방 삭제 (재제출 시)
        prescriptionRepository.deleteBySubmissionId(submission.getId());

        // 5. Submission 업데이트
        submission.submit(
            requestDto.primaryDiagnosis(),
            requestDto.subDiagnosis(),
            requestDto.finalJudgment()
        );
        Submission savedSubmission = submissionRepository.save(submission);

        // 6. 처방 저장
        List<Prescription> prescriptions = requestDto.prescriptions().stream()
                .map(prescriptionDto -> createPrescription(savedSubmission, prescriptionDto))
                .map(prescriptionRepository::save)
                .toList();

        // 7. 응답 DTO 생성
        List<PrescriptionResponseDto> prescriptionDtos = prescriptions.stream()
                .map(PrescriptionResponseDto::from)
                .toList();

        return SubmissionResponseDto.from(savedSubmission, prescriptionDtos);
    }
    

    
    private Prescription createPrescription(Submission submission, PrescriptionRequestDto dto) {
        return Prescription.builder()
                .submission(submission)
                .drugName(dto.drugName())
                .dosage(dto.dosage())
                .frequency(dto.frequency())
                .duration(dto.duration())
                .build();
    }
}
