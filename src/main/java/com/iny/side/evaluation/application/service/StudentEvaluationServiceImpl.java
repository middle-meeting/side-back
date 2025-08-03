package com.iny.side.evaluation.application.service;

import com.iny.side.chat.domain.entity.ChatMessage;
import com.iny.side.chat.domain.repository.ChatMessageRepository;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.evaluation.domain.entity.Evaluation;
import com.iny.side.evaluation.domain.repository.EvaluationRepository;
import com.iny.side.evaluation.web.dto.ChatFeedbackResultDto;
import com.iny.side.evaluation.web.dto.SummaryResponseDto;
import com.iny.side.submission.domain.entity.Prescription;
import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.repository.PrescriptionRepository;
import com.iny.side.submission.domain.repository.SubmissionRepository;
import com.iny.side.submission.web.dto.PrescriptionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentEvaluationServiceImpl implements StudentEvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final SubmissionRepository submissionRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional(readOnly = true)
    public SummaryResponseDto getMySummary(Long studentId, Long assignmentId) {
        // 1. 과제 수행 기록 조회
        Submission submission = submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElseThrow(() -> new NotFoundException("과제 수행 기록"));

        // 2. 해당 과제 수행에 대한 교수 채점이 있는지 조회
        Evaluation eval = evaluationRepository.findBySubmissionId(submission.getId())
                .orElseThrow(() -> new NotFoundException("평가"));

        // 3. 처방전 조회
        List<Prescription> prescriptions = prescriptionRepository.findBySubmissionId(submission.getId());
        if (prescriptions.isEmpty()) {
            throw new NotFoundException("처방전");
        }

        List<PrescriptionResponseDto> responseDtoList = prescriptions.stream()
                .map(PrescriptionResponseDto::from)
                .toList();

        // 4. 대화 내역 및 피드백 조회
        List<ChatMessage> chatMessages = chatMessageRepository.findBySubmissionIdOrderByTurnNumber(submission.getId());

        Map<Integer, List<ChatMessage>> chatMessagesByTurn = chatMessages.stream()
                .collect(Collectors.groupingBy(ChatMessage::getTurnNumber));
        List<ChatFeedbackResultDto> chatFeedbacks = chatMessagesByTurn.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    int turn = entry.getKey();
                    List<ChatMessage> pair = entry.getValue();

                    ChatMessage studentMessage = pair.stream()
                            .filter(msg -> msg.getSpeaker() == ChatMessage.SpeakerType.STUDENT)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("turnNumber " + turn + "에 학생 메시지가 없습니다."));
                    ChatMessage aiMessage = pair.stream()
                            .filter(msg -> msg.getSpeaker() == ChatMessage.SpeakerType.AI)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("turnNumber " + turn + "에 AI 메시지가 없습니다."));

                    return ChatFeedbackResultDto.from(studentMessage, aiMessage);
                })
                .toList();

        return SummaryResponseDto.from(submission, eval, responseDtoList, chatFeedbacks);
    }
}
