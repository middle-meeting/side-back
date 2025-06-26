package com.iny.side.users.application.service;

import com.iny.side.users.domain.entity.EmailVerification;
import com.iny.side.users.domain.repository.EmailVerificationRepository;
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
    private final VerificationCodeGenerator verificationCodeGenerator;
    private static final int EXPIRATION_MINUTES = 3;

    @Override
    @Transactional
    public void sendVerificationCode(EmailVerificationRequestDto requestDto) {
        String email = requestDto.email();
        
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
        
        // 실제 이메일 전송 로직 (현재는 로그로 대체)
        log.info("인증번호 전송 - 이메일: {}, 인증번호: {}", email, verificationCode);
        
        // TODO: 실제 이메일 전송 서비스 연동
        // emailSender.sendVerificationEmail(email, verificationCode);
    }

    @Override
    @Transactional
    public boolean verifyCode(EmailVerificationConfirmDto confirmDto) {
        String email = confirmDto.email();
        String code = confirmDto.verificationCode();
        
        Optional<EmailVerification> verificationOpt = 
                emailVerificationRepository.findByEmailAndVerificationCode(email, code);
        
        if (verificationOpt.isEmpty()) {
            return false;
        }
        
        EmailVerification verification = verificationOpt.get();
        
        if (verification.isExpired()) {
            return false;
        }
        
        if (verification.isVerified()) {
            return true;
        }
        
        verification.verify();
        emailVerificationRepository.save(verification);
        
        return true;
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
