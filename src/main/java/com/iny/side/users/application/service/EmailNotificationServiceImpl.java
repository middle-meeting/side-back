package com.iny.side.users.application.service;

import com.iny.side.users.application.port.EmailSender;
import com.iny.side.users.domain.event.EmailVerificationRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final EmailSender emailSender;

    @Override
    public void handleVerificationRequest(EmailVerificationRequestedEvent event) {
        emailSender.sendVerificationEmail(event.email(), event.verificationCode())
                .onSuccess(result -> log.info("인증번호 전송 성공 - 이메일: {}", event.email()))
                .onFailure(error -> {
                    log.error("인증번호 전송 실패 - 이메일: {}, 오류: {}", event.email(), error);
                    // 이메일 전송 실패는 비즈니스 로직에 영향을 주지 않음
                    // 필요시 재시도 로직이나 알림 등을 추가할 수 있음
                });
    }
}
