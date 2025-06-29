package com.iny.side.users.application.service;

import com.iny.side.common.exception.DuplicateUsernameException;
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
        String email = requestDto.email();

        // 이미 가입된 이메일인지 검증
        if (userRepository.existsByUsername(email)) {
            throw new DuplicateUsernameException(email);
        }

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

        log.info("인증번호 생성 완료 - 이메일: {}", email);
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
    @Transactional
    public void resendVerificationCode(EmailVerificationRequestDto requestDto) {
        sendVerificationCode(requestDto);
    }

    @Override
    public boolean isEmailVerified(String email) {
        return emailVerificationRepository.existsByEmailAndVerified(email, true);
    }


}
