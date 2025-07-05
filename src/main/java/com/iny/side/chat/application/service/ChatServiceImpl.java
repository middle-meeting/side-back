package com.iny.side.chat.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.chat.application.port.AiClient;
import com.iny.side.chat.domain.entity.ChatMessage;
import com.iny.side.chat.domain.repository.ChatMessageRepository;
import com.iny.side.chat.exception.AiResponseGenerationException;
import com.iny.side.chat.infrastructure.external.dto.AiChatRequestDto;
import com.iny.side.chat.infrastructure.external.dto.AiChatResponseDto;
import com.iny.side.chat.infrastructure.external.dto.ChatMessageSimpleDto;
import com.iny.side.chat.web.dto.ChatMessageResponseDto;
import com.iny.side.chat.web.dto.ChatResponseDto;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.common.result.Result;
import com.iny.side.course.application.service.EnrollmentValidationService;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final EnrollmentValidationService enrollmentValidationService;
    private final AiClient aiClient;

    @Override
    @Transactional
    public ChatResponseDto sendMessage(Long studentId, Long assignmentId, String message) {
        Account student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("학생"));
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new NotFoundException("과제"));

        enrollmentValidationService.validateStudentEnrolledInCourse(assignment.getCourse().getId(), studentId);

        // Submission 조회 또는 생성
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElseGet(() -> createNewSubmission(student, assignment));

        // 메세지 턴 번호 계산
        Integer currentTurnNumber = chatMessageRepository.findMaxTurnNumberBySubmissionId(submission.getId()) + 1;

        // 학생 메시지 저장
        ChatMessage studentMessage = saveStudentMessage(submission, currentTurnNumber, message);

        // 과거 대화내역 조회
        List<ChatMessageSimpleDto> savedChatMessages = chatMessageRepository.findBySubmissionIdOrderByTurnNumber(submission.getId())
                .stream().map(ChatMessageSimpleDto::from).toList();

        // AI 서버 호출
        AiChatRequestDto aiRequest = AiChatRequestDto.of(assignment.getPersonaName(), assignment.getPersonaAge(), assignment.getPersonaGender(),
                assignment.getPersonaSymptom(), assignment.getPersonaHistory(), assignment.getPersonaPersonality(),
                assignment.getPersonaDisease(), savedChatMessages);
        Result<AiChatResponseDto> aiResponseResult = aiClient.sendChatMessage(aiRequest);

        if (aiResponseResult.isFailure()) {
            log.error("AI 응답 생성 실패: {}", aiResponseResult.getErrorMessage());
            throw new AiResponseGenerationException(aiResponseResult.getErrorMessage());
        }

        // AI 응답 저장
        String aiResponse = aiResponseResult.getValue().answer();
        ChatMessage aiMessage = saveAiMessage(submission, currentTurnNumber, aiResponse);

        return ChatResponseDto.of(
                ChatMessageResponseDto.from(studentMessage),
                ChatMessageResponseDto.from(aiMessage)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponseDto> getMessages(Long studentId, Long assignmentId) {
        // 1. 과제 조회
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new NotFoundException("과제"));

        // 2. 학생이 해당 과제의 수강생인지 검증
        enrollmentValidationService.validateStudentEnrolledInCourse(assignment.getCourse().getId(), studentId);

        // 3. Submission 조회
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElseThrow(() -> new NotFoundException("과제 수행 기록"));

        // 4. 채팅 메시지들을 턴 번호 순으로 조회하여 DTO로 변환
        List<ChatMessage> chatMessages = chatMessageRepository.findBySubmissionIdOrderByTurnNumber(submission.getId());
        return chatMessages.stream()
                .map(ChatMessageResponseDto::from)
                .toList();
    }

    private Submission createNewSubmission(Account student, Assignment assignment) {
        Submission newSubmission = Submission.builder()
                .student(student)
                .assignment(assignment)
                .build();
        return submissionRepository.save(newSubmission);
    }

    private ChatMessage saveStudentMessage(Submission submission, Integer turnNumber, String message) {
        ChatMessage studentMessage = ChatMessage.builder()
                .submission(submission)
                .turnNumber(turnNumber)
                .speaker(ChatMessage.SpeakerType.STUDENT)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(studentMessage);
    }

    private ChatMessage saveAiMessage(Submission submission, Integer turnNumber, String message) {
        ChatMessage aiMessage = ChatMessage.builder()
                .submission(submission)
                .turnNumber(turnNumber)
                .speaker(ChatMessage.SpeakerType.AI)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(aiMessage);
    }

    private String createPatientContext(Assignment assignment) {
        return String.format(
                "환자 정보: 이름=%s, 나이=%d, 성별=%s, 증상=%s, 병력=%s, 성격=%s, 질병=%s",
                assignment.getPersonaName(),
                assignment.getPersonaAge(),
                assignment.getPersonaGender().getLabel(),
                assignment.getPersonaSymptom(),
                assignment.getPersonaHistory() != null ? assignment.getPersonaHistory() : "없음",
                assignment.getPersonaPersonality() != null ? assignment.getPersonaPersonality() : "보통",
                assignment.getPersonaDisease() != null ? assignment.getPersonaDisease() : "미상"
        );
    }
}
