package com.iny.side.users.infrastructure.external;

import com.iny.side.common.result.Result;
import com.iny.side.users.application.port.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Mailgun을 통한 이메일 전송 Adapter
 * EmailSender Port의 구현체
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MailgunEmailSender implements EmailSender {

    private final JavaMailSender mailSender;
    
    @Value("${app.mail.from:postmaster@sandbox-xxxxxx.mailgun.org}")
    private String fromEmail;
    
    @Value("${app.mail.verification.subject:이메일 인증번호}")
    private String verificationSubject;

    @Override
    public Result<Void> sendVerificationEmail(String to, String verificationCode) {
        try {
            SimpleMailMessage message = createVerificationMessage(to, verificationCode);
            mailSender.send(message);
            
            log.info("인증 이메일 전송 성공 - 수신자: {}", to);
            return Result.success(null);
            
        } catch (Exception e) {
            log.error("인증 이메일 전송 실패 - 수신자: {}, 오류: {}", to, e.getMessage(), e);
            return Result.failure("이메일 전송에 실패했습니다: " + e.getMessage(), e);
        }
    }

    private SimpleMailMessage createVerificationMessage(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(verificationSubject);
        message.setText(createVerificationEmailText(verificationCode));
        return message;
    }

    private String createVerificationEmailText(String verificationCode) {
        return String.format("""
                안녕하세요!
                
                회원가입을 위한 이메일 인증번호입니다.
                
                인증번호: %s
                
                이 인증번호는 3분간 유효합니다.
                인증번호를 입력하여 회원가입을 완료해주세요.
                
                감사합니다.
                """, verificationCode);
    }
}
