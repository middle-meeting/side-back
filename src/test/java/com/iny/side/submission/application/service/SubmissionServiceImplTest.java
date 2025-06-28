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
    void 과제_제출_성공() {
        // given
        Account student = TestFixtures.createStudent();
        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);
        
        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);
        
        // 기존 Submission 생성 (채팅을 통해 이미 생성되었다고 가정)
        Submission existingSubmission = TestFixtures.createSubmission(student, assignment);
        submissionRepository.save(existingSubmission);
        
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
        
        // when
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
}
