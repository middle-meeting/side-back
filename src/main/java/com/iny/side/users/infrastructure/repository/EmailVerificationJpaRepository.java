package com.iny.side.users.infrastructure.repository;

import com.iny.side.users.domain.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerification, Long> {
    
    Optional<EmailVerification> findByEmailAndVerificationCode(String email, String verificationCode);
    
    @Query("SELECT e FROM EmailVerification e WHERE e.email = :email ORDER BY e.createdAt DESC LIMIT 1")
    Optional<EmailVerification> findLatestByEmail(@Param("email") String email);

    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);

    void deleteByEmail(String email);
    
    boolean existsByEmailAndVerified(String email, Boolean verified);
}
