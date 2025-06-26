package com.iny.side.users.infrastructure.repository;

import com.iny.side.users.domain.entity.EmailVerification;
import com.iny.side.users.domain.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {

    private final EmailVerificationJpaRepository emailVerificationJpaRepository;

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailVerificationJpaRepository.save(emailVerification);
    }

    @Override
    public Optional<EmailVerification> findByEmailAndVerificationCode(String email, String verificationCode) {
        return emailVerificationJpaRepository.findByEmailAndVerificationCode(email, verificationCode);
    }

    @Override
    public Optional<EmailVerification> findLatestByEmail(String email) {
        return emailVerificationJpaRepository.findLatestByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        emailVerificationJpaRepository.deleteByEmail(email);
    }

    @Override
    public boolean existsByEmailAndVerified(String email, Boolean verified) {
        return emailVerificationJpaRepository.existsByEmailAndVerified(email, verified);
    }
}
