package com.okatu.rgan.user.repository;

import com.okatu.rgan.user.feature.model.entity.FollowRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {
    Optional<FollowRelationship> findByBeFollowedIdAndFollowerIdAndTypeAndStatus(Long beFollowedId, Long followerId, Integer type, Integer status);
}
