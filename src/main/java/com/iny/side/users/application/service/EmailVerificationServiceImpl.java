package com.iny.side.users.application.service;

import com.iny.side.common.exception.DuplicateUsernameException;
import com.iny.side.common.exception.EmailResendTooSoonException;
import com.iny.side.common.exception.InvalidVerificationCodeException;
import com.iny.side.users.domain.entity.EmailVerification;
import com.iny.side.users.domain.event.EmailVerificationRequestedEvent;
import com.iny.side.users.domain.repository.EmailVerificationRepository;
import com.iny.side.users.domain.repository.UserRepository;
import com.iny.side.users.web.dto.EmailVerificationConfirmDto;
import com.iny.side.users.web.dto.EmailVerificationRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final EmailNotificationService emailNotificationService;
    private static final int EXPIRATION_MINUTES = 3;

    @Override
    @Transactional
    public void sendVerificationCode(EmailVerificationRequestDto requestDto) {
        sendVerificationCodeInternal(requestDto);
    }

    @Override
    @Transactional
    public void verifyCode(EmailVerificationConfirmDto confirmDto) {
        String email = confirmDto.email();
        String code = confirmDto.verificationCode();

        Optional<EmailVerification> verificationOpt =
                emailVerificationRepository.findByEmailAndVerificationCode(email, code);

        if (verificationOpt.isEmpty()) {
            throw new InvalidVerificationCodeException();
        }

        EmailVerification verification = verificationOpt.get();

        if (verification.isExpired()) {
            throw new InvalidVerificationCodeException();
        }

        if (verification.isVerified()) {
            return; // 이미 인증된 경우 성공으로 처리
        }

        verification.verify();
        emailVerificationRepository.save(verification);
    }

    @Override
    public boolean isEmailVerified(String email) {
        return emailVerificationRepository.existsByEmailAndVerified(email, true);
    }

    /**
     * 인증번호 전송의 공통 로직을 처리합니다.
     * 모든 전송 요청에 대해 3분 제한을 적용하여 스팸 방지를 보장합니다.
     *
     * @param requestDto 이메일 인증 요청 정보
     */
    private void sendVerificationCodeInternal(EmailVerificationRequestDto requestDto) {
        String email = requestDto.email();

        // 이미 가입된 이메일인지 검증
        if (userRepository.existsByUsername(email)) {
            throw new DuplicateUsernameException(email);
        }

        // 모든 전송 요청에 대해 3분 제한 검증 (스팸 방지)
        validateResendTimeLimit(email);

        // 기존 인증 코드 삭제
        emailVerificationRepository.deleteByEmail(email);

        // 새 인증 코드 생성
        String verificationCode = verificationCodeGenerator.generateCode();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(EXPIRATION_MINUTES);

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .createdAt(now)
                .expiresAt(expiresAt)
                .build();

        emailVerificationRepository.save(emailVerification);

        // 도메인 이벤트 발행 (이메일 전송은 별도 처리)
        EmailVerificationRequestedEvent event = new EmailVerificationRequestedEvent(email, verificationCode);
        emailNotificationService.handleVerificationRequest(event);

        log.info("인증번호 전송 완료 - 이메일: {}", email);
    }

    /**
     * 이메일 재전송 시간 제한을 검증합니다.
     * 마지막 전송으로부터 3분이 지나지 않았다면 예외를 발생시킵니다.
     *
     * @param email 검증할 이메일
     * @throws EmailResendTooSoonException 3분이 지나지 않은 경우
     */
    private void validateResendTimeLimit(String email) {
        Optional<EmailVerification> latestVerification =
                emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(email);

        if (latestVerification.isEmpty()) {
            return; // 기존 인증 기록이 없으면 전송 가능
        }

        EmailVerification verification = latestVerification.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastSentAt = verification.getCreatedAt();

        // 마지막 전송으로부터 3분이 지났는지 확인
        LocalDateTime allowedResendTime = lastSentAt.plusMinutes(EXPIRATION_MINUTES);

        if (now.isBefore(allowedResendTime)) {
            long remainingSeconds = java.time.Duration.between(now, allowedResendTime).getSeconds();
            throw new EmailResendTooSoonException(remainingSeconds);
        }
    }

}
