package com.iny.side.submission.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.common.SliceResponse;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.application.service.EnrollmentValidationService;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import com.iny.side.submission.domain.vo.PrescriptionInfo;
import com.iny.side.submission.domain.vo.SubmissionDetailVo;
import com.iny.side.submission.web.dto.*;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.PrescriptionRepository;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorSubmissionServiceImpl implements ProfessorSubmissionService {

    private final SubmissionRepository submissionRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AssignmentRepository assignmentRepository;
    private final EnrollmentValidationService enrollmentValidationService;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional(readOnly = true)
    public ProfessorSubmissionResponseDto get(Long professorId, Long assignmentId, Long studentId) {
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

        return ProfessorSubmissionResponseDto.from(submission, prescriptionResponseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public SubmissionStatusResponseDto getStatus(Long accountId, Long courseId, Long assignmentId) {
        // 1. 강의에 대한 교수의 권한 체크
        Course course = enrollmentValidationService.validateProfessorOwnsCourse(courseId, accountId);

        // 2. 전체 수강생 수 조회
        Long enrolledCount = enrollmentRepository.countAllByCourseId(course.getId());

        // 3. 채점완료, 채점필요, 미제출 수 조회
        Long evaluatedCount = submissionRepository.countEvaluatedByCourseIdAndAssignmentId(course.getId(), assignmentId);
        Long evaluationRequiredCount = submissionRepository.countNotEvaluatedByCourseIdAndAssignmentId(course.getId(), assignmentId);
        Long notSubmittedCount = submissionRepository.countNotSubmittedByCourseIdAndAssignmentId(course.getId(), assignmentId);

        return new SubmissionStatusResponseDto(enrolledCount, evaluatedCount, evaluationRequiredCount, notSubmittedCount);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<ProfessorSubmissionDetailResponseDto> getAll(Long accountId, Long courseId, Long assignmentId, SubmissionFilter status, int page) {
        // 1. 강의에 대한 교수의 권한 체크
        Course course = enrollmentValidationService.validateProfessorOwnsCourse(courseId, accountId);

        // 2. Prescription 제외한 DTO 조회
        Pageable pageable = PageRequest.of(page, 12);
        Slice<SubmissionDetailVo> submissionDetailVoSlice = getSubmissionDetailVoSliceByStatus(course.getId(), assignmentId, status, pageable);

        // 3. Prescription 일괄 조회
        List<Long> submissionIds = submissionDetailVoSlice
                .stream()
                .map(SubmissionDetailVo::submissionId)
                .toList();
        List<PrescriptionInfo> prescriptionInfoList = prescriptionRepository.findBySubmissionIdIn(submissionIds)
                .stream()
                .map(PrescriptionInfo::from)
                .toList();

        // 4. submissionId별로 묶기
        Map<Long, List<PrescriptionInfo>> prescriptionMap = prescriptionInfoList
                .stream()
                .collect(Collectors.groupingBy(PrescriptionInfo::submissionId));

        // 5. submissionDetailVoSlice, prescriptionMap 합성해 ProfessorSubmissionDetailResponseDto 생성
        List<ProfessorSubmissionDetailResponseDto> professorSubmissionDetailResponseDtoList = submissionDetailVoSlice.map(vo -> {
            List<PrescriptionResponseDto> prescriptions = prescriptionMap
                    .getOrDefault(vo.submissionId(), List.of())
                    .stream()
                    .map(PrescriptionResponseDto::from).toList();

            return ProfessorSubmissionDetailResponseDto.from(vo, prescriptions);
        }).toList();

        return SliceResponse.of(professorSubmissionDetailResponseDtoList, page, 12, submissionDetailVoSlice.hasNext());
    }

    private Slice<SubmissionDetailVo> getSubmissionDetailVoSliceByStatus(Long courseId, Long assignmentId, SubmissionFilter status, Pageable pageable) {
        return switch (status) {
            case ALL ->
                    submissionRepository.findAllByCourseIdAndAssignmentId(courseId, assignmentId, pageable);
            case GRADED ->
                    submissionRepository.findEvaluatedByCourseIdAndAssignmentId(courseId, assignmentId, pageable);
            case NEEDS_GRADING ->
                    submissionRepository.findEvaluationRequiredByCourseIdAndAssignmentId(courseId, assignmentId, pageable);
            case NOT_SUBMITTED ->
                    submissionRepository.findNotSubmittedByCourseIdAndAssignmentId(courseId, assignmentId, pageable);
        };
    }
}
