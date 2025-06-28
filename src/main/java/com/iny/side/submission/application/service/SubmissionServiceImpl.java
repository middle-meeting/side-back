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

        // 3. 기존 Submission 조회 (채팅을 통해 이미 생성된 DRAFT 상태)
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElseThrow(() -> new NotFoundException("과제 수행 기록"));

        // 4. Submission 상태 변경 및 내용 채우기 (DRAFT → SUBMITTED)
        submission.submit(
                requestDto.primaryDiagnosis(),
                requestDto.subDiagnosis(),
                requestDto.finalJudgment()
        );
        Submission savedSubmission = submissionRepository.save(submission);

        // 5. 처방 정보 업데이트 (Delete & Insert 방식)
        List<Prescription> prescriptions = updatePrescriptions(savedSubmission, requestDto.prescriptions());

        // 6. 응답 DTO 생성
        List<PrescriptionResponseDto> prescriptionDtos = prescriptions.stream()
                .map(PrescriptionResponseDto::from)
                .toList();

        return SubmissionResponseDto.from(savedSubmission, prescriptionDtos);
    }


    /**
     * 처방 정보를 업데이트합니다.
     * 기존 처방을 모두 삭제하고 새로운 처방을 생성합니다. (Delete & Insert 방식)
     *
     * @param submission 제출 정보
     * @param prescriptionDtos 새로운 처방 정보 목록
     * @return 생성된 처방 목록
     */
    private List<Prescription> updatePrescriptions(Submission submission, List<PrescriptionRequestDto> prescriptionDtos) {
        // 기존 처방 삭제 (있다면)
        List<Prescription> existingPrescriptions = prescriptionRepository.findBySubmissionId(submission.getId());
        if (!existingPrescriptions.isEmpty()) {
            prescriptionRepository.deleteBySubmissionId(submission.getId());
        }

        // 새 처방 생성 및 저장
        return prescriptionDtos.stream()
                .map(dto -> createPrescription(submission, dto))
                .map(prescriptionRepository::save)
                .toList();
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
