package com.iny.side.users.mock;

import com.iny.side.users.application.service.VerificationCodeGenerator;
import lombok.Getter;
import lombok.Setter;

public class FakeVerificationCodeGenerator implements VerificationCodeGenerator {
    
    @Setter
    private String fixedCode = "123456";
    @Getter
    private int callCount = 0;

    @Override
    public String generateCode() {
        callCount++;
        return fixedCode;
    }

    public void resetCallCount() {
        this.callCount = 0;
    }
}
