package com.okatu.rgan.user.repository;

import com.okatu.rgan.user.feature.model.entity.FollowRelationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {
    Optional<FollowRelationship> findByBeFollowedIdAndFollowerIdAndTypeAndStatus(Long beFollowedId, Long followerId, Integer type, Integer status);


    Set<FollowRelationship> findByFollowerIdAndTypeAndStatus(Long followerId, Integer type, Integer status);

    Page<FollowRelationship> findByBeFollowedIdAndTypeAndStatus(Long beFollowedId, Integer type, Integer status, Pageable pageable);

    Page<FollowRelationship> findByFollowerIdAndTypeAndStatus(Long followerId, Integer type, Integer status, Pageable pageable);
}
