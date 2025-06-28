package com.iny.side.submission.application.service;

import com.iny.side.TestFixtures;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.mock.FakeEnrollmentRepository;
import com.iny.side.course.mock.FakeEnrollmentValidationService;
import com.iny.side.submission.domain.entity.Prescription;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubmissionServiceImplTest {
    
    private SubmissionService submissionService;
    private FakeSubmissionRepository submissionRepository;
    private FakePrescriptionRepository prescriptionRepository;
    private FakeAssignmentRepository assignmentRepository;
    private FakeEnrollmentRepository enrollmentRepository;
    private FakeEnrollmentValidationService enrollmentValidationService;
    
    @BeforeEach
    void setUp() {
        submissionRepository = new FakeSubmissionRepository();
        prescriptionRepository = new FakePrescriptionRepository();
        assignmentRepository = new FakeAssignmentRepository();
        enrollmentRepository = new FakeEnrollmentRepository();
        enrollmentValidationService = new FakeEnrollmentValidationService(enrollmentRepository, null);

        submissionService = new SubmissionServiceImpl(
                submissionRepository,
                prescriptionRepository,
                assignmentRepository,
                enrollmentValidationService
        );
    }
    
    @Test
    void 과제_최종_제출_성공() {
        // given
        Account student = TestFixtures.createStudent();
        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);

        // 기존 DRAFT 상태의 Submission 생성 (채팅을 통해 이미 생성됨)
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

        // when - DRAFT 상태의 Submission에 진단 정보를 채우고 SUBMITTED로 상태 변경
        SubmissionResponseDto response = submissionService.submit(student.getId(), assignment.getId(), requestDto);

        // then
        assertThat(response.primaryDiagnosis()).isEqualTo("감기");
        assertThat(response.subDiagnosis()).isEqualTo("인후염");
        assertThat(response.finalJudgment()).isEqualTo("충분한 휴식과 수분 섭취를 권장합니다.");
        assertThat(response.prescriptions()).hasSize(2);
        assertThat(response.prescriptions().get(0).drugName()).isEqualTo("아세트아미노펜");
        assertThat(response.prescriptions().get(1).drugName()).isEqualTo("이부프로펜");
        assertThat(response.status()).isEqualTo("SUBMITTED");
        assertThat(response.submittedAt()).isNotNull();

        // 기존 Submission이 업데이트되었는지 확인 (새로 생성되지 않음)
        assertThat(submissionRepository.findByStudentIdAndAssignmentId(student.getId(), assignment.getId()))
                .isPresent()
                .get()
                .satisfies(submission -> {
                    assertThat(submission.getId()).isEqualTo(draftSubmission.getId()); // 동일한 ID
                    assertThat(submission.getStatus()).isEqualTo(Submission.SubmissionStatus.SUBMITTED);
                });
    }
    
    @Test
    void 수강하지_않은_과제는_제출_불가() {
        // given
        Account student = TestFixtures.createStudent();
        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);
        
        // 수강 등록하지 않음
        
        List<PrescriptionRequestDto> prescriptions = List.of(
                new PrescriptionRequestDto("아세트아미노펜", "500mg", "1일 3회", "7일")
        );
        
        SubmissionRequestDto requestDto = new SubmissionRequestDto(
                "감기",
                null,
                prescriptions,
                "충분한 휴식과 수분 섭취를 권장합니다."
        );
        
        // when & then
        assertThatThrownBy(() -> submissionService.submit(student.getId(), assignment.getId(), requestDto))
                .isInstanceOf(ForbiddenException.class);
    }
    
    @Test
    void Submission이_없으면_제출_불가() {
        // given
        Account student = TestFixtures.createStudent();
        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);
        
        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);
        
        // Submission 생성하지 않음 (채팅을 하지 않았다고 가정)
        
        List<PrescriptionRequestDto> prescriptions = List.of(
                new PrescriptionRequestDto("아세트아미노펜", "500mg", "1일 3회", "7일")
        );
        
        SubmissionRequestDto requestDto = new SubmissionRequestDto(
                "감기",
                null,
                prescriptions,
                "충분한 휴식과 수분 섭취를 권장합니다."
        );
        
        // when & then
        assertThatThrownBy(() -> submissionService.submit(student.getId(), assignment.getId(), requestDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 기존_처방이_있는_경우_새_처방으로_교체() {
        // given
        Account student = TestFixtures.createStudent();
        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);

        // DRAFT 상태의 Submission 생성
        Submission draftSubmission = TestFixtures.createSubmission(student, assignment);
        submissionRepository.save(draftSubmission);

        // 기존 처방 생성 (이전에 임시 저장했다고 가정)
        Prescription existingPrescription = Prescription.builder()
                .submission(draftSubmission)
                .drugName("기존약물")
                .dosage("100mg")
                .frequency("1일 1회")
                .duration("3일")
                .build();
        prescriptionRepository.save(existingPrescription);

        // 새로운 처방 정보
        List<PrescriptionRequestDto> newPrescriptions = List.of(
                new PrescriptionRequestDto("새약물1", "200mg", "1일 2회", "5일"),
                new PrescriptionRequestDto("새약물2", "300mg", "1일 3회", "7일")
        );

        SubmissionRequestDto requestDto = new SubmissionRequestDto(
                "새진단",
                null,
                newPrescriptions,
                "새로운 판단"
        );

        // when
        SubmissionResponseDto response = submissionService.submit(student.getId(), assignment.getId(), requestDto);

        // then
        // 기존 처방은 삭제되고 새 처방만 존재해야 함
        assertThat(response.prescriptions()).hasSize(2);
        assertThat(response.prescriptions())
                .extracting("drugName")
                .containsExactly("새약물1", "새약물2");

        // 데이터베이스에서도 확인
        List<Prescription> savedPrescriptions = prescriptionRepository.findBySubmissionId(draftSubmission.getId());
        assertThat(savedPrescriptions).hasSize(2);
        assertThat(savedPrescriptions)
                .extracting(Prescription::getDrugName)
                .containsExactly("새약물1", "새약물2");
    }
}
