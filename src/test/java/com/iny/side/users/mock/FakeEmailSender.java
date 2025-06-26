package com.iny.side.users.mock;

import com.iny.side.common.result.Result;
import com.iny.side.users.application.port.EmailSender;

import java.util.ArrayList;
import java.util.List;

public class FakeEmailSender implements EmailSender {

    private final List<SentEmail> sentEmails = new ArrayList<>();
    private boolean shouldFail = false;

    @Override
    public Result<Void> sendVerificationEmail(String to, String verificationCode) {
        if (shouldFail) {
            return Result.failure("이메일 전송 실패 시뮬레이션");
        }

        sentEmails.add(new SentEmail(to, verificationCode));
        return Result.success(null);
    }

    public List<SentEmail> getSentEmails() {
        return new ArrayList<>(sentEmails);
    }

    public void clear() {
        sentEmails.clear();
    }

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    public boolean wasEmailSentTo(String email) {
        return sentEmails.stream()
                .anyMatch(sentEmail -> sentEmail.to().equals(email));
    }

    public String getLastVerificationCodeSentTo(String email) {
        return sentEmails.stream()
                .filter(sentEmail -> sentEmail.to().equals(email))
                .reduce((first, second) -> second) // 마지막 이메일
                .map(SentEmail::verificationCode)
                .orElse(null);
    }

    public int getEmailCount() {
        return sentEmails.size();
    }

    public record SentEmail(String to, String verificationCode) {}
}
