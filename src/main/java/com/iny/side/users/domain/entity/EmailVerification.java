package com.iny.side.users.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

    @Builder
    public EmailVerification(String email, String verificationCode, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.verified = false;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public void verify() {
        this.verified = true;
    }

    public boolean isVerified() {
        return this.verified;
    }
}
