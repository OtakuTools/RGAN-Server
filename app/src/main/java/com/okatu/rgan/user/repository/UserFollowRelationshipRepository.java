package com.okatu.rgan.user.repository;

import com.okatu.rgan.user.feature.model.entity.UserFollowRelationship;
import com.okatu.rgan.user.feature.model.entity.UserFollowRelationshipId;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFollowRelationshipRepository extends JpaRepository<UserFollowRelationship, UserFollowRelationshipId> {
    List<UserFollowRelationship> findById_FollowerAndStatus(RganUser follower, Integer status);

    Page<UserFollowRelationship> findById_FollowerAndStatusIs(RganUser follower, Integer status, Pageable pageable);

    Page<UserFollowRelationship> findById_BeFollowedAndStatus(RganUser beFollowed, Integer status, Pageable pageable);

    int countById_BeFollowedAndStatus(RganUser beFollowed, Integer status);

    int countById_FollowerAndStatus(RganUser follower, Integer status);
}
