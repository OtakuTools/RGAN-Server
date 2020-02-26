package com.okatu.rgan.user.repository;

import com.okatu.rgan.user.model.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    Optional<UserVerification> findByToken(String token);

    Optional<UserVerification> findByUsername(String username);
}
