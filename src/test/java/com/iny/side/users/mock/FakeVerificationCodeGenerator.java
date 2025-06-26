package com.iny.side.users.mock;

import com.iny.side.users.application.service.VerificationCodeGenerator;

public class FakeVerificationCodeGenerator implements VerificationCodeGenerator {

    private String fixedCode = "123456";
    private int callCount = 0;

    @Override
    public String generateCode() {
        callCount++;
        return fixedCode;
    }

    public void setFixedCode(String code) {
        this.fixedCode = code;
    }

    public int getCallCount() {
        return callCount;
    }

    public void resetCallCount() {
        this.callCount = 0;
    }
}
