package com.iny.side.submission.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.application.service.EnrollmentValidationService;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.PrescriptionRepository;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import com.iny.side.submission.web.dto.PrescriptionResponseDto;
import com.iny.side.submission.web.dto.SubmissionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorSubmissionServiceImpl implements ProfessorSubmissionService {

    private final SubmissionRepository submissionRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AssignmentRepository assignmentRepository;
    private final EnrollmentValidationService enrollmentValidationService;

    @Override
    public SubmissionResponseDto get(Long professorId, Long assignmentId, Long studentId) {
        // 1. 과제 조회
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new NotFoundException("과제"));

        // 2. 과제에 대한 학생과 교수의 권한 확인
        enrollmentValidationService.validateStudentEnrolledInCourse(assignment.getCourse().getId(), studentId);
        enrollmentValidationService.validateProfessorOwnsCourse(assignment.getCourse().getId(), professorId);

        // 3. 제출물 조회
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElseThrow(() -> new NotFoundException("과제 수행 기록"));

        // 4. 처방 조회
        List<PrescriptionResponseDto> prescriptionResponseDtoList = prescriptionRepository.findBySubmissionId(submission.getId())
                .stream()
                .map(PrescriptionResponseDto::from)
                .toList();

        return SubmissionResponseDto.from(submission, prescriptionResponseDtoList);
    }
}
