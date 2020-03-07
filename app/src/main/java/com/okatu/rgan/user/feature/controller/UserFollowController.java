package com.okatu.rgan.user.feature.controller;

import com.okatu.rgan.common.exception.EntityNotFoundException;
import com.okatu.rgan.user.feature.constant.FollowRelationshipStatus;
import com.okatu.rgan.user.feature.constant.FollowRelationshipType;
import com.okatu.rgan.user.feature.model.entity.FollowRelationship;
import com.okatu.rgan.user.feature.model.param.FollowParam;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.RganUserDTO;
import com.okatu.rgan.user.repository.FollowRelationshipRepository;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow")
public class UserFollowController {

    @Autowired
    private FollowRelationshipRepository followRelationshipRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    public String follow(FollowParam param, @AuthenticationPrincipal RganUser user){

        if(!userRepository.findById(param.getTargetUserId()).isPresent()){
            throw new EntityNotFoundException("user", param.getTargetUserId());
        }

        FollowRelationship followRelationship = new FollowRelationship();
        followRelationship.setBeFollowedId(param.getTargetUserId());
        followRelationship.setFollowerId(user.getId());
        followRelationship.setStatus(FollowRelationshipStatus.FOLLOWING);
        followRelationship.setType(FollowRelationshipType.USER);

        followRelationshipRepository.save(followRelationship);

        return "";
    }

    @DeleteMapping("/user")
    public String unFollow(FollowParam param, @AuthenticationPrincipal RganUser user){

        followRelationshipRepository.findByBeFollowedIdAndFollowerIdAndTypeAndStatus(
            param.getTargetUserId(), user.getId(), FollowRelationshipType.USER, FollowRelationshipStatus.FOLLOWING).ifPresent(followRelationship -> {
            followRelationship.setStatus(FollowRelationshipStatus.UN_FOLLOW);
            followRelationshipRepository.save(followRelationship);
        });

        return "";
    }
}
