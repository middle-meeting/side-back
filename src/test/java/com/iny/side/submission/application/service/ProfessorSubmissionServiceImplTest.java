package com.iny.side.submission.application.service;

import com.iny.side.TestFixtures;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.course.mock.FakeEnrollmentRepository;
import com.iny.side.course.mock.FakeEnrollmentValidationService;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.mock.FakePrescriptionRepository;
import com.iny.side.submission.mock.FakeSubmissionRepository;
import com.iny.side.submission.web.dto.PrescriptionRequestDto;
import com.iny.side.submission.web.dto.SubmissionRequestDto;
import com.iny.side.submission.web.dto.SubmissionResponseDto;
import com.iny.side.users.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfessorSubmissionServiceImplTest {

    private ProfessorSubmissionService professorSubmissionService;
    private SubmissionService submissionService;
    private FakeCourseRepository courseRepository;
    private FakeSubmissionRepository submissionRepository;
    private FakePrescriptionRepository prescriptionRepository;
    private FakeAssignmentRepository assignmentRepository;
    private FakeEnrollmentRepository enrollmentRepository;
    private FakeEnrollmentValidationService enrollmentValidationService;

    @BeforeEach
    void setUp() {
        courseRepository = new FakeCourseRepository();
        submissionRepository = new FakeSubmissionRepository();
        prescriptionRepository = new FakePrescriptionRepository();
        assignmentRepository = new FakeAssignmentRepository();
        enrollmentRepository = new FakeEnrollmentRepository();
        enrollmentValidationService = new FakeEnrollmentValidationService(enrollmentRepository, courseRepository);

        professorSubmissionService = new ProfessorSubmissionServiceImpl(
                submissionRepository,
                prescriptionRepository,
                assignmentRepository,
                enrollmentValidationService
        );

        submissionService = new SubmissionServiceImpl(
                submissionRepository,
                prescriptionRepository,
                assignmentRepository,
                enrollmentValidationService
        );
    }

    @Test
    void 과제_제출물_조회() {
        // given
        Account student = TestFixtures.student(1L);
        Account professor = TestFixtures.professor(2L);

        Course course = TestFixtures.course(1L, professor);
        courseRepository.save(course);

        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);

        Submission draftSubmission = TestFixtures.createSubmission(student, assignment);
        submissionRepository.save(draftSubmission);

        List<PrescriptionRequestDto> prescriptions = List.of(
                new PrescriptionRequestDto("아세트아미노펜", "500mg", "1일 3회", "7일"),
                new PrescriptionRequestDto("이부프로펜", "200mg", "1일 2회", "5일")
        );

        SubmissionRequestDto requestDto = new SubmissionRequestDto(
                "감기",
                "인후염",
                prescriptions,
                "충분한 휴식과 수분 섭취를 권장합니다."
        );

        // DRAFT 상태의 Submission에 진단 정보를 채우고 SUBMITTED로 상태 변경
        SubmissionResponseDto response = submissionService.submit(student.getId(), assignment.getId(), requestDto);

        // when
        SubmissionResponseDto submissionResponseDto = professorSubmissionService.get(professor.getId(), assignment.getId(), student.getId());

        // then
        assertThat(submissionResponseDto.primaryDiagnosis()).isEqualTo(response.primaryDiagnosis());
        assertThat(submissionResponseDto.subDiagnosis()).isEqualTo(response.subDiagnosis());
        assertThat(submissionResponseDto.finalJudgment()).isEqualTo(response.finalJudgment());
        assertThat(submissionResponseDto.prescriptions()).hasSize(response.prescriptions().size());
        assertThat(submissionResponseDto.prescriptions().get(0).drugName()).isEqualTo(response.prescriptions().get(0).drugName());
        assertThat(submissionResponseDto.prescriptions().get(1).drugName()).isEqualTo(response.prescriptions().get(1).drugName());
        assertThat(submissionResponseDto.status()).isEqualTo(response.status());
        assertThat(submissionResponseDto.submittedAt()).isNotNull();
    }
}
