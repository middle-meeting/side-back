package com.iny.side.chat.application.service;

import com.iny.side.TestFixtures;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.chat.exception.AiResponseGenerationException;
import com.iny.side.chat.mock.FakeAiClient;
import com.iny.side.chat.mock.FakeChatMessageRepository;
import com.iny.side.chat.web.dto.ChatMessageRequestDto;
import com.iny.side.chat.web.dto.ChatMessageResponseDto;
import com.iny.side.chat.web.dto.ChatResponseDto;
import com.iny.side.common.domain.GenderType;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.mock.FakeEnrollmentRepository;
import com.iny.side.course.mock.FakeEnrollmentValidationService;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.mock.FakeSubmissionRepository;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChatServiceImplTest {
    
    private ChatServiceImpl chatService;
    private FakeChatMessageRepository chatMessageRepository;
    private FakeSubmissionRepository submissionRepository;
    private FakeAssignmentRepository assignmentRepository;
    private FakeUserRepository userRepository;
    private FakeEnrollmentRepository enrollmentRepository;
    private FakeEnrollmentValidationService enrollmentValidationService;
    private FakeAiClient aiClient;
    
    @BeforeEach
    void setUp() {
        chatMessageRepository = new FakeChatMessageRepository();
        submissionRepository = new FakeSubmissionRepository();
        assignmentRepository = new FakeAssignmentRepository();
        userRepository = new FakeUserRepository();
        enrollmentRepository = new FakeEnrollmentRepository();
        enrollmentValidationService = new FakeEnrollmentValidationService(enrollmentRepository, null);
        aiClient = new FakeAiClient();

        chatService = new ChatServiceImpl(
                chatMessageRepository,
                submissionRepository,
                assignmentRepository,
                userRepository,
                enrollmentValidationService,
                aiClient
        );
    }
    
    @Test
    void 채팅_메시지_전송_성공() {
        // given
        Account student = TestFixtures.createStudent();
        userRepository.save(student);

        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);

        String message = "안녕하세요, 어떤 증상이 있으신가요?";

        // when
        ChatResponseDto response = chatService.sendMessage(student.getId(), assignment.getId(), message);

        // then
        assertThat(response).isNotNull();
        assertThat(response.studentMessage()).isNotNull();
        assertThat(response.aiMessage()).isNotNull();
        assertThat(response.studentMessage().message()).isEqualTo(message);
        assertThat(response.studentMessage().speaker()).isEqualTo("STUDENT");
        assertThat(response.aiMessage().speaker()).isEqualTo("AI");
        assertThat(response.studentMessage().turnNumber()).isEqualTo(1);
        assertThat(response.aiMessage().turnNumber()).isEqualTo(1);
    }
    
    @Test
    void 수강하지_않은_과제에_채팅_전송시_예외_발생() {
        // given
        Account student = TestFixtures.createStudent();
        userRepository.save(student);

        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록하지 않음

        String message = "안녕하세요";

        // when & then
        assertThatThrownBy(() -> chatService.sendMessage(student.getId(), assignment.getId(), message))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 존재하지_않는_학생_ID로_채팅_전송시_예외_발생() {
        // given
        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        String message = "안녕하세요";

        // when & then
        assertThatThrownBy(() -> chatService.sendMessage(999L, assignment.getId(), message))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 존재하지_않는_과제_ID로_채팅_전송시_예외_발생() {
        // given
        Account student = TestFixtures.createStudent();
        userRepository.save(student);

        String message = "안녕하세요";

        // when & then
        assertThatThrownBy(() -> chatService.sendMessage(student.getId(), 999L, message))
                .isInstanceOf(NotFoundException.class);
    }
    
    @Test
    void FastAPI_서버_호출_실패시_예외_발생() {
        // given
        Account student = TestFixtures.createStudent();
        userRepository.save(student);

        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);

        aiClient.setShouldFail(true);

        String message = "안녕하세요";

        // when & then
        assertThatThrownBy(() -> chatService.sendMessage(student.getId(), assignment.getId(), message))
                .isInstanceOf(AiResponseGenerationException.class);
    }

    @Test
    void 채팅_메시지_조회_성공() {
        // given
        Account student = TestFixtures.createStudent();
        userRepository.save(student);

        Course course = TestFixtures.createCourse();
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

        // when
        List<ChatMessageResponseDto> messages = chatService.getMessages(student.getId(), assignment.getId());

        // then
        assertThat(messages).hasSize(4); // 학생 메시지 2개 + AI 응답 2개
        assertThat(messages.get(0).speaker()).isEqualTo("STUDENT");
        assertThat(messages.get(0).message()).isEqualTo(message1);
        assertThat(messages.get(1).speaker()).isEqualTo("AI");
        assertThat(messages.get(2).speaker()).isEqualTo("STUDENT");
        assertThat(messages.get(2).message()).isEqualTo(message2);
        assertThat(messages.get(3).speaker()).isEqualTo("AI");
    }

    @Test
    void 채팅_메시지_조회_Submission이_없으면_예외_발생() {
        // given
        Account student = TestFixtures.createStudent();
        userRepository.save(student);

        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);

        // when & then
        assertThatThrownBy(() -> chatService.getMessages(student.getId(), assignment.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 채팅_메시지_조회_수강하지_않은_과제는_접근_불가() {
        // given
        Account student = TestFixtures.createStudent();
        userRepository.save(student);

        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록하지 않음

        // when & then
        assertThatThrownBy(() -> chatService.getMessages(student.getId(), assignment.getId()))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 첫_채팅_시_Submission_자동_생성() {
        // given
        Account student = TestFixtures.createStudent();
        userRepository.save(student);

        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);

        String message = "안녕하세요, 어떤 증상이 있으신가요?";

        // Submission이 없는 상태에서 시작
        assertThat(submissionRepository.findByStudentIdAndAssignmentId(student.getId(), assignment.getId()))
                .isEmpty();

        // when
        ChatResponseDto response = chatService.sendMessage(student.getId(), assignment.getId(), message);

        // then
        assertThat(response).isNotNull();
        assertThat(response.studentMessage().message()).isEqualTo(message);
        assertThat(response.aiMessage()).isNotNull();

        // Submission이 자동으로 생성되었는지 확인
        assertThat(submissionRepository.findByStudentIdAndAssignmentId(student.getId(), assignment.getId()))
                .isPresent()
                .get()
                .satisfies(submission -> {
                    assertThat(submission.getStudent().getId()).isEqualTo(student.getId());
                    assertThat(submission.getAssignment().getId()).isEqualTo(assignment.getId());
                    assertThat(submission.getStatus()).isEqualTo(Submission.SubmissionStatus.DRAFT);
                    assertThat(submission.getPrimaryDiagnosis()).isNull(); // 아직 제출 전
                    assertThat(submission.getFinalJudgment()).isNull(); // 아직 제출 전
                });
    }

    @Test
    void 이미_Submission이_있으면_새로_생성하지_않음() {
        // given
        Account student = TestFixtures.createStudent();
        userRepository.save(student);

        Course course = TestFixtures.createCourse();
        Assignment assignment = TestFixtures.createAssignment(course);
        assignmentRepository.save(assignment);

        // 수강 등록
        Enrollment enrollment = TestFixtures.createEnrollment(course, student);
        enrollmentRepository.save(enrollment);

        // 기존 Submission 생성
        Submission existingSubmission = TestFixtures.createSubmission(student, assignment);
        submissionRepository.save(existingSubmission);
        Long originalSubmissionId = existingSubmission.getId();

        String message = "추가 질문이 있습니다.";

        // when
        ChatResponseDto response = chatService.sendMessage(student.getId(), assignment.getId(), message);

        // then
        assertThat(response).isNotNull();
        assertThat(response.studentMessage().message()).isEqualTo(message);

        // 기존 Submission을 재사용했는지 확인 (새로 생성하지 않음)
        assertThat(submissionRepository.findByStudentIdAndAssignmentId(student.getId(), assignment.getId()))
                .isPresent()
                .get()
                .satisfies(submission -> {
                    assertThat(submission.getId()).isEqualTo(originalSubmissionId);
                });
    }
}
