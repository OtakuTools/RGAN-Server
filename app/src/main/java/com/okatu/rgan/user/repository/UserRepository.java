package com.okatu.rgan.user.repository;

import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<RganUser, Long> {
    Optional<RganUser> findByUsername(String username);

}
