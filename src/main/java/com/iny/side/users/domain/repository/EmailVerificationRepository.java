package com.iny.side.users.domain.repository;

import com.iny.side.users.domain.entity.EmailVerification;

import java.util.Optional;

public interface EmailVerificationRepository {
    EmailVerification save(EmailVerification emailVerification);
    
    Optional<EmailVerification> findByEmailAndVerificationCode(String email, String verificationCode);
    
    Optional<EmailVerification> findLatestByEmail(String email);
    
    void deleteByEmail(String email);
    
    boolean existsByEmailAndVerified(String email, Boolean verified);
}
