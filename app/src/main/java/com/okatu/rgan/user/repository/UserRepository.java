package com.okatu.rgan.user.repository;

import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<RganUser, Long> {
    Optional<RganUser> findByUsername(String username);

    Optional<RganUser> findByUsernameOrEmail(String username, String email);

    Optional<RganUser> findByEmail(String email);

    Optional<RganUser> findByVerificationToken(String token);

    List<RganUser> findByEmailOrVerificationEmail(String email, String verificationEmail);

//    Page<RganUser> findById(Pageable pageable);
}
