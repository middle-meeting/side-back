package com.iny.side.users.application.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomVerificationCodeGenerator implements VerificationCodeGenerator {
    
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private final Random random = new Random();

    @Override
    public String generateCode() {
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }
}
