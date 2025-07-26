package com.iny.side.evaluation.application.service;

import com.iny.side.TestFixtures;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.chat.application.service.ChatServiceImpl;
import com.iny.side.chat.domain.entity.ChatMessage;
import com.iny.side.chat.mock.FakeAiClient;
import com.iny.side.chat.mock.FakeChatMessageRepository;
import com.iny.side.chat.web.dto.ChatMessageResponseDto;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.course.mock.FakeEnrollmentRepository;
import com.iny.side.course.mock.FakeEnrollmentValidationService;
import com.iny.side.evaluation.mock.FakeEvaluationRepository;
import com.iny.side.evaluation.web.dto.*;
import com.iny.side.submission.application.service.SubmissionService;
import com.iny.side.submission.application.service.SubmissionServiceImpl;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.mock.FakePrescriptionRepository;
import com.iny.side.submission.mock.FakeSubmissionRepository;
import com.iny.side.submission.web.dto.PrescriptionRequestDto;
import com.iny.side.submission.web.dto.SubmissionRequestDto;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfessorEvaluationServiceImplTest {

    private ProfessorEvaluationServiceImpl professorEvaluationService;
    private SubmissionService submissionService;
    private ChatServiceImpl chatService;
    private FakeEnrollmentValidationService enrollmentValidationService;
    private FakeEvaluationRepository evaluationRepository;
    private FakeSubmissionRepository submissionRepository;
    private FakeChatMessageRepository chatMessageRepository;
    private FakeEnrollmentRepository enrollmentRepository;
    private FakeCourseRepository courseRepository;
    private FakeAssignmentRepository assignmentRepository;
    private FakePrescriptionRepository prescriptionRepository;
    private FakeUserRepository userRepository;
    private FakeAiClient aiClient;

    @BeforeEach
    void setUp() {
        evaluationRepository = new FakeEvaluationRepository();
        submissionRepository = new FakeSubmissionRepository();
        chatMessageRepository = new FakeChatMessageRepository();
        enrollmentRepository = new FakeEnrollmentRepository();
        courseRepository = new FakeCourseRepository();
        assignmentRepository = new FakeAssignmentRepository();
        enrollmentValidationService = new FakeEnrollmentValidationService(enrollmentRepository, courseRepository);
        prescriptionRepository = new FakePrescriptionRepository();
        userRepository = new FakeUserRepository();
        aiClient = new FakeAiClient();

        submissionService = new SubmissionServiceImpl(
                submissionRepository,
                prescriptionRepository,
                assignmentRepository,
                enrollmentValidationService
        );

        chatService = new ChatServiceImpl(
                chatMessageRepository,
                submissionRepository,
                assignmentRepository,
                userRepository,
                enrollmentValidationService,
                aiClient
        );

        professorEvaluationService = new ProfessorEvaluationServiceImpl(
                evaluationRepository,
                enrollmentValidationService,
                submissionRepository,
                chatMessageRepository
        );
    }

    @Test
    void 과제_채점() {
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
        submissionService.submit(student.getId(), assignment.getId(), requestDto);

        EvaluationRequestDto evaluationRequestDto = new EvaluationRequestDto(100, "참잘했어요");

        // when
        EvaluationResponseDto evaluationResponseDto = professorEvaluationService.evaluate(professor.getId(), course.getId(), assignment.getId(), student.getId(), evaluationRequestDto);

        // then
        assertThat(evaluationResponseDto.id()).isEqualTo(1L);
        assertThat(evaluationResponseDto.score()).isEqualTo(evaluationRequestDto.score());
        assertThat(evaluationResponseDto.feedback()).isEqualTo(evaluationRequestDto.feedback());

        assertThat(evaluationRepository.findById(evaluationResponseDto.id()))
                .isPresent()
                .get()
                .satisfies(evaluation -> {
                    assertThat(evaluation.getSubmission()).isEqualTo(submissionRepository.findById(draftSubmission.getId()).get());
                    assertThat(evaluation.getProfessor()).isEqualTo(professor);
                });
    }

    @Test
    void 과제의_대화내역_채점() {
        // given
        Account student = TestFixtures.student(1L);
        userRepository.save(student);
        Account professor = TestFixtures.professor(2L);
        userRepository.save(professor);

        Course course = TestFixtures.course(1L, professor);
        courseRepository.save(course);

        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);

        // 채팅 메시지 전송하여 데이터 생성
        String message1 = "안녕하세요, 어떤 증상이 있으신가요?";
        String message2 = "추가 질문이 있습니다.";

        chatService.sendMessage(student.getId(), assignment.getId(), message1);
        chatService.sendMessage(student.getId(), assignment.getId(), message2);

        List<ChatMessage> chatMessages = chatMessageRepository.findBySubmissionIdOrderByTurnNumber(1L);
        List<ChatMessageResponseDto> list = chatMessages.stream()
                .filter(chatMessage -> chatMessage.getSpeaker().equals(ChatMessage.SpeakerType.STUDENT))
                .map(ChatMessageResponseDto::from)
                .toList();

        ChatsEvaluationRequestDto requestDto = new ChatsEvaluationRequestDto(
                list.stream()
                        .map(chatMessageResponseDto -> new ChatEvaluationRequestDto(chatMessageResponseDto.id(), 100, "참 잘했어요"))
                        .toList());

        // when
        List<ChatEvaluationResponseDto> responseDtoList = professorEvaluationService.evaluateChats(professor.getId(), course.getId(), assignment.getId(), student.getId(), requestDto);

        // then
        assertThat(responseDtoList).hasSize(2);
        assertThat(responseDtoList.get(0).score()).isEqualTo(100);
        assertThat(responseDtoList.get(0).feedback()).isEqualTo("참 잘했어요");
        assertThat(responseDtoList.get(1).score()).isEqualTo(100);
        assertThat(responseDtoList.get(1).feedback()).isEqualTo("참 잘했어요");
    }
}
