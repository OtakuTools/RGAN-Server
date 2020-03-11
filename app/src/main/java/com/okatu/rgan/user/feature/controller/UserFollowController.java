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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;


// The unsubscibe logic have two choice, delete or keep the history data from one’s inbox.
// We decide to keep them.
// thus, in the aggregate layer, we have to do filter:
// for each post, double check that whether current following users contain the post's author
@RestController
@RequestMapping("/follow")
public class UserFollowController {

    @Autowired
    private FollowRelationshipRepository followRelationshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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

        // fill the initial inbox

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
