package com.iny.side.users.mock;

import com.iny.side.users.domain.entity.EmailVerification;
import com.iny.side.users.domain.repository.EmailVerificationRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeEmailVerificationRepository implements EmailVerificationRepository {

    private final List<EmailVerification> data = new ArrayList<>();

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        data.add(emailVerification);
        return emailVerification;
    }

    @Override
    public Optional<EmailVerification> findByEmailAndVerificationCode(String email, String verificationCode) {
        return data.stream()
                .filter(verification -> Objects.equals(verification.getEmail(), email) 
                        && Objects.equals(verification.getVerificationCode(), verificationCode))
                .findFirst();
    }

    @Override
    public Optional<EmailVerification> findLatestByEmail(String email) {
        return data.stream()
                .filter(verification -> Objects.equals(verification.getEmail(), email))
                .max(Comparator.comparing(EmailVerification::getCreatedAt));
    }

    @Override
    public Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email) {
        return data.stream()
                .filter(verification -> Objects.equals(verification.getEmail(), email))
                .max(Comparator.comparing(EmailVerification::getCreatedAt));
    }

    @Override
    public void deleteByEmail(String email) {
        data.removeIf(verification -> Objects.equals(verification.getEmail(), email));
    }

    @Override
    public boolean existsByEmailAndVerified(String email, Boolean verified) {
        return data.stream()
                .anyMatch(verification -> Objects.equals(verification.getEmail(), email) 
                        && Objects.equals(verification.getVerified(), verified));
    }
}
