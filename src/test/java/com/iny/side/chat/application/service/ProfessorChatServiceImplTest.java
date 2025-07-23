package com.iny.side.chat.application.service;

import com.iny.side.TestFixtures;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.chat.mock.FakeAiClient;
import com.iny.side.chat.mock.FakeChatMessageRepository;
import com.iny.side.chat.web.dto.ChatMessageResponseDto;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.mock.FakeCourseRepository;
import com.iny.side.course.mock.FakeEnrollmentRepository;
import com.iny.side.course.mock.FakeEnrollmentValidationService;
import com.iny.side.submission.mock.FakeSubmissionRepository;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProfessorChatServiceImplTest {

    private ProfessorChatServiceImpl professorChatServiceImpl;
    private ChatServiceImpl chatService;
    private FakeChatMessageRepository chatMessageRepository;
    private FakeCourseRepository courseRepository;
    private FakeSubmissionRepository submissionRepository;
    private FakeAssignmentRepository assignmentRepository;
    private FakeUserRepository userRepository;
    private FakeEnrollmentRepository enrollmentRepository;
    private FakeEnrollmentValidationService enrollmentValidationService;
    private FakeAiClient aiClient;

    @BeforeEach
    void setUp() {
        chatMessageRepository = new FakeChatMessageRepository();
        courseRepository = new FakeCourseRepository();
        submissionRepository = new FakeSubmissionRepository();
        assignmentRepository = new FakeAssignmentRepository();
        userRepository = new FakeUserRepository();
        enrollmentRepository = new FakeEnrollmentRepository();
        enrollmentValidationService = new FakeEnrollmentValidationService(enrollmentRepository, courseRepository);
        aiClient = new FakeAiClient();

        chatService = new ChatServiceImpl(
                chatMessageRepository,
                submissionRepository,
                assignmentRepository,
                userRepository,
                enrollmentValidationService,
                aiClient
        );

        professorChatServiceImpl = new ProfessorChatServiceImpl(
                chatMessageRepository,
                submissionRepository,
                assignmentRepository,
                enrollmentValidationService
        );
    }

    @Test
    void 과제의_대화내역_조회() {
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

        // when
        List<ChatMessageResponseDto> messages = professorChatServiceImpl.getMessages(professor.getId(), assignment.getId(), student.getId());

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
    void 본인의_강의가_아닌_과제의_대화내역_조회시_예외_발생() {
        // given
        Account student = TestFixtures.student(1L);
        userRepository.save(student);

        Account professor = TestFixtures.professor(2L);
        userRepository.save(professor);

        Course course = TestFixtures.course(1L, TestFixtures.professor(3L));
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

        // when & then
        assertThatThrownBy(() -> professorChatServiceImpl.getMessages(professor.getId(), assignment.getId(), student.getId()))
                .isInstanceOf(ForbiddenException.class);
    }
}
