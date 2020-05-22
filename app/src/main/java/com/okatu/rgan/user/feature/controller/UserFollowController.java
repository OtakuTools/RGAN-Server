package com.okatu.rgan.user.feature.controller;

import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.user.feature.constant.UserFollowRelationshipStatus;
import com.okatu.rgan.user.feature.model.entity.UserFollowRelationship;
import com.okatu.rgan.user.feature.model.entity.UserFollowRelationshipId;
import com.okatu.rgan.user.feature.model.param.FollowParam;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.UserFollowRelationshipRepository;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.Formatter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


// The unsubscibe logic have two choice, delete or keep the history data from one’s inbox.
// We decide to keep them.
// thus, in the aggregate layer, we have to do filter:
// for each post, double check that whether current following users contain the post's author
@RestController
@RequestMapping("/follow")
public class UserFollowController {

    @Autowired
    private UserFollowRelationshipRepository userFollowRelationshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/user")
    public Boolean checkIfFollowingUser(@RequestParam(value = "targetUserId") Long userId, @AuthenticationPrincipal RganUser user) {
        RganUser beFollowed = userRepository.findById(userId).orElseThrow(
            () -> new ResourceNotFoundException("user", userId)
        );

        return userFollowRelationshipRepository.findById(new UserFollowRelationshipId(beFollowed, user))
            .filter(userFollowRelationship -> userFollowRelationship.getStatus().equals(UserFollowRelationshipStatus.FOLLOWING))
            .isPresent();
    }

    @PostMapping("/user")
    public String follow(@RequestBody FollowParam param, @AuthenticationPrincipal RganUser user){

        RganUser beFollowed = userRepository.findById(param.getTargetUserId()).orElseThrow(
            () -> new ResourceNotFoundException("user", param.getTargetUserId())
        );

        UserFollowRelationshipId id = new UserFollowRelationshipId(beFollowed, user);

        Optional<UserFollowRelationship> optional = userFollowRelationshipRepository.findById(new UserFollowRelationshipId(beFollowed, user));
        if(optional.isPresent()){
            UserFollowRelationship userFollowRelationship = optional.get();
            if(userFollowRelationship.getStatus().equals(UserFollowRelationshipStatus.UN_FOLLOW)){
                userFollowRelationship.setStatus(UserFollowRelationshipStatus.FOLLOWING);
                userFollowRelationshipRepository.save(userFollowRelationship);
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have been following this user!");
            }
        }else {
            UserFollowRelationship userFollowRelationship = new UserFollowRelationship(id, UserFollowRelationshipStatus.FOLLOWING);
            userFollowRelationshipRepository.save(userFollowRelationship);
        }

        return "";
    }

    @DeleteMapping("/user")
    public String unFollow(@RequestBody FollowParam param, @AuthenticationPrincipal RganUser user){

        RganUser beFollowed = userRepository.findById(param.getTargetUserId()).orElseThrow(
            () -> new ResourceNotFoundException("user", param.getTargetUserId())
        );
        UserFollowRelationshipId id = new UserFollowRelationshipId(userRepository.getOne(param.getTargetUserId()), user);
        userFollowRelationshipRepository.findById(id).ifPresent(
            userFollowRelationship -> {
                userFollowRelationship.setStatus(UserFollowRelationshipStatus.UN_FOLLOW);
                userFollowRelationshipRepository.save(userFollowRelationship);
            });

        return "";
    }
}
