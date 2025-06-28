package com.iny.side.chat.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.chat.domain.entity.ChatMessage;
import com.iny.side.chat.domain.repository.ChatMessageRepository;
import com.iny.side.chat.exception.AiResponseGenerationException;
import com.iny.side.chat.infrastructure.external.AiClient;
import com.iny.side.chat.infrastructure.external.dto.AiChatRequestDto;
import com.iny.side.chat.infrastructure.external.dto.AiChatResponseDto;
import com.iny.side.chat.web.dto.ChatMessageRequestDto;
import com.iny.side.chat.web.dto.ChatMessageResponseDto;
import com.iny.side.chat.web.dto.ChatResponseDto;
import com.iny.side.common.exception.ForbiddenException;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.common.result.Result;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    
    private final ChatMessageRepository chatMessageRepository;
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AiClient aiClient;
    
    @Override
    @Transactional
    public ChatResponseDto sendMessage(Long studentId, ChatMessageRequestDto requestDto) {
        // 1. 학생 조회
        Account student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("학생"));
        
        // 2. 과제 조회
        Assignment assignment = assignmentRepository.findByAssignmentId(requestDto.assignmentId())
                .orElseThrow(() -> new NotFoundException("과제"));

        // 3. 학생이 해당 과제의 수강생인지 검증
        validateStudentEnrolledInCourse(assignment.getCourse().getId(), studentId);

        // 4. Submission 조회 또는 생성
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, requestDto.assignmentId())
                .orElseGet(() -> createNewSubmission(student, assignment));

        // 5. 현재 턴 번호 계산
        Integer currentTurnNumber = chatMessageRepository.findMaxTurnNumberBySubmissionId(submission.getId()) + 1;

        // 6. 학생 메시지 저장
        ChatMessage studentMessage = saveStudentMessage(submission, currentTurnNumber, requestDto.message());

        // 7. 환자 컨텍스트 생성
        String patientContext = createPatientContext(assignment);

        // 8. AI 서버 호출
        AiChatRequestDto aiRequest = AiChatRequestDto.of(requestDto.message(), patientContext);
        Result<AiChatResponseDto> aiResponseResult = aiClient.sendChatMessage(aiRequest);
        
        if (aiResponseResult.isFailure()) {
            log.error("AI 응답 생성 실패: {}", aiResponseResult.getErrorMessage());
            throw new AiResponseGenerationException(aiResponseResult.getErrorMessage());
        }

        // 9. AI 응답 저장
        String aiResponse = aiResponseResult.getValue().response();
        ChatMessage aiMessage = saveAiMessage(submission, currentTurnNumber, aiResponse);

        // 10. 응답 DTO 생성
        return ChatResponseDto.of(
            ChatMessageResponseDto.from(studentMessage),
            ChatMessageResponseDto.from(aiMessage)
        );
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
    
    private void validateStudentEnrolledInCourse(Long courseId, Long studentId) {
        enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new ForbiddenException("forbidden.not_enrolled"));
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
