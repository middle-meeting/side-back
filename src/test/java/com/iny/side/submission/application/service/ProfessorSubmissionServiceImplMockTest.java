package com.iny.side.submission.application.service;

import com.iny.side.TestFixtures;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.course.application.service.EnrollmentValidationService;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import com.iny.side.submission.domain.entity.Prescription;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.PrescriptionRepository;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import com.iny.side.submission.domain.vo.SubmissionDetailVo;
import com.iny.side.submission.web.dto.PrescriptionResponseDto;
import com.iny.side.submission.web.dto.SubmissionFilter;
import com.iny.side.submission.web.dto.SubmissionStatusResponseDto;
import com.iny.side.users.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfessorSubmissionServiceImplMockTest {

    @Mock
    private EnrollmentValidationService enrollmentValidationService;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private ProfessorSubmissionServiceImpl professorSubmissionServiceImpl;

    private Account professor;
    private Account student;
    private Course course;
    private Assignment assignment;
    private Submission submission;

    @BeforeEach
    void setUp() {
        professor = TestFixtures.professor(2L);
        student = TestFixtures.createStudent();
        course = TestFixtures.course(1L, professor);
        assignment = TestFixtures.createAssignment(course);
        submission = TestFixtures.createSubmission(student, assignment);
    }

    @Test
    void 제출_및_채점_현황조회() {
        // given
        when(enrollmentValidationService.validateProfessorOwnsCourse(course.getId(), professor.getId()))
                .thenReturn(course);

        when(enrollmentRepository.countAllByCourseId(course.getId()))
                .thenReturn(10L);

        when(submissionRepository.countEvaluatedByCourseIdAndAssignmentId(course.getId(), assignment.getId()))
                .thenReturn(4L);
        when(submissionRepository.countNotEvaluatedByCourseIdAndAssignmentId(course.getId(), assignment.getId()))
                .thenReturn(3L);
        when(submissionRepository.countNotSubmittedByCourseIdAndAssignmentId(course.getId(), assignment.getId()))
                .thenReturn(3L);

        // when
        SubmissionStatusResponseDto dto =
                professorSubmissionServiceImpl.getStatus(professor.getId(), course.getId(), assignment.getId());

        // then
        assertThat(dto.enrolledCount()).isEqualTo(10L);
        assertThat(dto.evaluatedCount()).isEqualTo(4L);
        assertThat(dto.evaluationRequiredCount()).isEqualTo(3L);
        assertThat(dto.notSubmittedCount()).isEqualTo(3L);
    }

    @Test
    void 과제_제출_목록_조회() {
        // given
        when(enrollmentValidationService.validateProfessorOwnsCourse(course.getId(), professor.getId()))
                .thenReturn(course);

        Pageable pageable = PageRequest.of(0, 12);

        SubmissionDetailVo vo1 = new SubmissionDetailVo(
                1L,
                "홍길동",
                "20171234",
                "hong@test.com",
                1L,
                LocalDateTime.of(2025,8,4,12,0),
                6L,
                85,
                "급성 심근경색"
        );

        // 서브미션 전체 조회 stub
        when(submissionRepository.findAllByCourseIdAndAssignmentId(course.getId(), assignment.getId(), pageable))
                .thenReturn(new SliceImpl<>(List.of(vo1), pageable, false));

        Prescription prescription1 = new Prescription(
                1L,
                submission,
                "아스피린",
                "500mg",
                "1일3회",
                "5일"
        );

        Prescription prescription2 = new Prescription(
                2L,
                submission,
                "클로피도그렐",
                "500mg",
                "1일3회",
                "5일"
        );
        List<Prescription> prescriptions = List.of(prescription1, prescription2);
        List<PrescriptionResponseDto> prescriptionResponseDtoList = prescriptions.stream()
                .map(PrescriptionResponseDto::from)
                .toList();
        when(prescriptionRepository.findBySubmissionIdIn(List.of(1L)))
                .thenReturn(prescriptions);

        // when
        var sliceResponse = professorSubmissionServiceImpl.getAll(
                professor.getId(),
                course.getId(),
                assignment.getId(),
                SubmissionFilter.ALL,
                0
        );

        // then
        var list = sliceResponse.getContent();
        assertThat(list).hasSize(1);
        var dto = list.get(0);
        assertThat(dto.accountId()).isEqualTo(vo1.accountId());
        assertThat(dto.studentName()).isEqualTo(vo1.studentName());
        assertThat(dto.score()).isEqualTo(vo1.score());
        assertThat(dto.primaryDiagnosis()).isEqualTo(vo1.primaryDiagnosis());
        assertThat(dto.prescriptions()).isEqualTo(prescriptionResponseDtoList);
    }
}
