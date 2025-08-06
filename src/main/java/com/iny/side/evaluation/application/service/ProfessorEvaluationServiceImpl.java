package com.iny.side.evaluation.application.service;

import com.iny.side.chat.domain.entity.ChatMessage;
import com.iny.side.chat.domain.repository.ChatMessageRepository;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.application.service.EnrollmentValidationService;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.evaluation.domain.entity.Evaluation;
import com.iny.side.evaluation.domain.repository.EvaluationRepository;
import com.iny.side.evaluation.web.dto.*;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorEvaluationServiceImpl implements ProfessorEvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final EnrollmentValidationService enrollmentValidationService;
    private final SubmissionRepository submissionRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional(readOnly = true)
    public EvaluationResponseDto get(Long accountId, Long courseId, Long assignmentId, Long studentId) {
        // 1. 강의에 대한 교수와 학생의 권한 체크
        Course course = enrollmentValidationService.validateProfessorOwnsCourse(courseId, accountId);
        enrollmentValidationService.validateStudentEnrolledInCourse(courseId, studentId);

        // 2. 제출물 조회
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElseThrow(() -> new NotFoundException("과제 제출 기록"));

        // 3. 평가 내역 조회
        Evaluation evaluation = evaluationRepository.findBySubmissionIdAndAccountId(submission.getId(), accountId)
                .orElseThrow(() -> new NotFoundException("과제 평가 기록"));

        return EvaluationResponseDto.from(evaluation);
    }

    @Override
    @Transactional
    public EvaluationResponseDto evaluate(Long accountId, Long courseId, Long assignmentId, Long studentId, EvaluationRequestDto evaluationRequestDto) {
        // 1. 강의에 대한 교수와 학생의 권한 체크
        Course course = enrollmentValidationService.validateProfessorOwnsCourse(courseId, accountId);
        enrollmentValidationService.validateStudentEnrolledInCourse(courseId, studentId);

        // 2. 제출물 조회
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElseThrow(() -> new NotFoundException("과제 제출 기록"));

        // 3. 평가 엔티티 생성
        Evaluation evaluation = Evaluation.create(submission, course.getAccount(), evaluationRequestDto);

        return EvaluationResponseDto.from(evaluationRepository.save(evaluation));
    }

    @Override
    @Transactional
    public List<ChatEvaluationResponseDto> evaluateChats(Long accountId, Long courseId, Long assignmentId, Long studentId, ChatsEvaluationRequestDto evaluationRequestDto) {
        // 1. 강의에 대한 교수와 학생의 권한 체크
        enrollmentValidationService.validateProfessorOwnsCourse(courseId, accountId);
        enrollmentValidationService.validateStudentEnrolledInCourse(courseId, studentId);

        // 2. 대화에 대한 평가 업데이트
        List<ChatMessage> chatMessages = evaluationRequestDto.chatEvaluationRequestDtoList()
                .stream()
                .map(chatEvaluationRequestDto -> {
                    ChatMessage chatMessage = chatMessageRepository.findById(chatEvaluationRequestDto.id())
                            .orElseThrow(() -> new NotFoundException("대화 내용"));

                    chatMessage.validateEvaluable();
                    chatMessage.evaluate(chatEvaluationRequestDto.score(), chatEvaluationRequestDto.feedback());

                    return chatMessageRepository.save(chatMessage);
                })
                .toList();

        return chatMessages.stream().map(ChatEvaluationResponseDto::from).toList();
    }
}
