package com.iny.side.evaluation.application.service;

import com.iny.side.TestFixtures;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.evaluation.domain.entity.Evaluation;
import com.iny.side.evaluation.mock.FakeEvaluationRepository;
import com.iny.side.evaluation.web.dto.SummaryResponseDto;
import com.iny.side.submission.domain.entity.Prescription;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.mock.FakePrescriptionRepository;
import com.iny.side.submission.mock.FakeSubmissionRepository;
import com.iny.side.users.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudentEvaluationServiceImplTest {

    private FakeEvaluationRepository evaluationRepository;
    private FakeSubmissionRepository submissionRepository;
    private FakePrescriptionRepository prescriptionRepository;
    private StudentEvaluationService studentEvaluationService;

    private Account student;
    private Account professor;
    private Course course;
    private Assignment assignment;
    private Submission submission;
    private Evaluation evaluation;

    @BeforeEach
    void setUp() {
        // Repository 초기화
        evaluationRepository = new FakeEvaluationRepository();
        submissionRepository = new FakeSubmissionRepository();
        prescriptionRepository = new FakePrescriptionRepository();

        // 서비스 초기화
        studentEvaluationService = new StudentEvaluationServiceImpl(
                evaluationRepository,
                submissionRepository,
                prescriptionRepository
        );

        // 테스트 데이터 생성
        professor = TestFixtures.createProfessor();
        student = TestFixtures.createStudent();
        course = TestFixtures.createCourse();
        assignment = TestFixtures.createAssignment(course);

        // 제출물 생성
        submission = Submission.builder()
                .student(student)
                .assignment(assignment)
                .primaryDiagnosis("감기")
                .subDiagnosis("두통")
                .finalJudgment("충분한 휴식과 수분 섭취를 권장합니다.")
                .status(Submission.SubmissionStatus.SUBMITTED)
                .build();
        submission = submissionRepository.save(submission);

        // 평가 생성
        evaluation = Evaluation.builder()
                .score(85)
                .feedback("좋은 진단이에요.")
                .isVisible(true)
                .submission(submission)
                .professor(professor)
                .build();
        evaluation = evaluationRepository.save(evaluation);

        // 처방전 생성
        Prescription prescription = Prescription.builder()
                .submission(submission)
                .drugName("아세트아미노펜")
                .dosage("500mg")
                .frequency("1일 3회")
                .duration("7일")
                .build();
        prescriptionRepository.save(prescription);
    }

    @Test
    void 학생이_자신의_과제_평가를_조회한다() {
        // when
        SummaryResponseDto result = studentEvaluationService.getMySummary(student.getId(), assignment.getId());

        // then
        assertThat(result.score()).isEqualTo(evaluation.getScore());
        assertThat(result.feedback()).isEqualTo(evaluation.getFeedback());
        assertThat(result.primaryDiagnosis()).isEqualTo(submission.getPrimaryDiagnosis());
        assertThat(result.subDiagnosis()).isEqualTo(submission.getSubDiagnosis());
        assertThat(result.finalJudgment()).isEqualTo(submission.getFinalJudgment());
        assertThat(result.prescriptions()).hasSize(1);
        assertThat(result.prescriptions().get(0).drugName()).isEqualTo("아세트아미노펜");
    }

    @Test
    void 존재하지_않는_과제_수행_기록_조회시_예외_발생() {
        // given
        Long nonExistentAssignmentId = 999L;

        // when & then
        assertThatThrownBy(() -> studentEvaluationService.getMySummary(student.getId(), nonExistentAssignmentId))
                .isInstanceOf(NotFoundException.class)
                .satisfies(exception -> {
                    NotFoundException ex = (NotFoundException) exception;
                    assertThat((String) ex.getArgs()[0]).isEqualTo("과제 수행 기록");
                });
    }

    @Test
    void 아직_채점이_되지_않았으면_예외_발생() {
        // given
        Assignment assignment1 = TestFixtures.createAssignment(2L, course);
        Submission nonExistentEvalSubmission = Submission.builder()
                .student(student)
                .assignment(assignment1)
                .primaryDiagnosis("감기")
                .subDiagnosis("두통")
                .finalJudgment("충분한 휴식과 수분 섭취를 권장합니다.")
                .status(Submission.SubmissionStatus.SUBMITTED)
                .build();
        submissionRepository.save(nonExistentEvalSubmission);

        // when & then
        assertThatThrownBy(() -> studentEvaluationService.getMySummary(student.getId(), assignment1.getId()))
                .isInstanceOf(NotFoundException.class)
                .satisfies(exception -> {
                    NotFoundException ex = (NotFoundException) exception;
                    assertThat((String) ex.getArgs()[0]).isEqualTo("평가");
                });
    }

    @Test
    void 최종진단서에_처방전이_없으면_예외_발생() {
        // given
        // 처방전 삭제
        prescriptionRepository.deleteBySubmissionId(submission.getId());

        // when & then
        assertThatThrownBy(() -> studentEvaluationService.getMySummary(student.getId(), assignment.getId()))
                .isInstanceOf(NotFoundException.class)
                .satisfies(exception -> {
                    NotFoundException ex = (NotFoundException) exception;
                    assertThat((String) ex.getArgs()[0]).isEqualTo("처방전");
                });
    }
}
