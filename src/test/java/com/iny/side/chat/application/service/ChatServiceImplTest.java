package com.iny.side.chat.application.service;

import com.iny.side.TestFixtures;
import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.mock.FakeAssignmentRepository;
import com.iny.side.chat.exception.AiResponseGenerationException;
import com.iny.side.chat.mock.FakeAiClient;
import com.iny.side.chat.mock.FakeChatMessageRepository;
import com.iny.side.chat.web.dto.ChatMessageRequestDto;
import com.iny.side.chat.web.dto.ChatResponseDto;
import com.iny.side.common.domain.GenderType;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.mock.FakeEnrollmentRepository;
import com.iny.side.submission.mock.FakeSubmissionRepository;
import com.iny.side.users.domain.Role;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChatServiceImplTest {
    
    private ChatServiceImpl chatService;
    private FakeChatMessageRepository chatMessageRepository;
    private FakeSubmissionRepository submissionRepository;
    private FakeAssignmentRepository assignmentRepository;
    private FakeUserRepository userRepository;
    private FakeEnrollmentRepository enrollmentRepository;
    private FakeAiClient aiClient;
    
    @BeforeEach
    void setUp() {
        chatMessageRepository = new FakeChatMessageRepository();
        submissionRepository = new FakeSubmissionRepository();
        assignmentRepository = new FakeAssignmentRepository();
        userRepository = new FakeUserRepository();
        enrollmentRepository = new FakeEnrollmentRepository();
        aiClient = new FakeAiClient();

        chatService = new ChatServiceImpl(
                chatMessageRepository,
                submissionRepository,
                assignmentRepository,
                userRepository,
                enrollmentRepository,
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
}
