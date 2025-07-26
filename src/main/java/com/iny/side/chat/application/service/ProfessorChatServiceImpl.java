package com.iny.side.chat.application.service;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.chat.domain.entity.ChatMessage;
import com.iny.side.chat.domain.repository.ChatMessageRepository;
import com.iny.side.chat.web.dto.ChatMessageResponseDto;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.application.service.EnrollmentValidationService;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorChatServiceImpl implements ProfessorChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final EnrollmentValidationService enrollmentValidationService;

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponseDto> getMessages(Long professorId, Long assignmentId, Long studentId) {
        // 1. 과제 조회
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new NotFoundException("과제"));

        // 2. 과제에 대한 학생과 교수의 권한 확인
        enrollmentValidationService.validateStudentEnrolledInCourse(assignment.getCourse().getId(), studentId);
        enrollmentValidationService.validateProfessorOwnsCourse(assignment.getCourse().getId(), professorId);

        // 3. 제출물 조회
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElseThrow(() -> new NotFoundException("과제 수행 기록"));

        // 4. 채팅 메시지들을 턴 번호 순으로 조회하여 DTO로 변환
        List<ChatMessage> chatMessages = chatMessageRepository.findBySubmissionIdOrderByTurnNumber(submission.getId());
        return chatMessages.stream()
                .map(ChatMessageResponseDto::from)
                .toList();
    }
}
