package com.iny.side.users.application.port;

import com.iny.side.common.result.Result;

/**
 * 이메일 전송을 위한 Output Port
 * 외부 이메일 서비스(Mailgun, SendGrid, AWS SES 등)와의 연동 인터페이스
 */
public interface EmailSender {
    /**
     * 이메일 인증번호를 전송합니다.
     * 
     * @param to 수신자 이메일 주소
     * @param verificationCode 인증번호
     * @return 전송 결과 (성공/실패)
     */
    Result<Void> sendVerificationEmail(String to, String verificationCode);
}
