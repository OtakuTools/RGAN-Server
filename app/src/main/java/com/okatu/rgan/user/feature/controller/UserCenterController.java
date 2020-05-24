package com.okatu.rgan.user.feature.controller;

import com.okatu.rgan.blog.constant.BlogStatus;
import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.service.BlogService;
import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.user.feature.constant.UserFollowRelationshipStatus;
import com.okatu.rgan.user.feature.model.param.UserProfileEditParam;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.RganUserDTO;
import com.okatu.rgan.user.repository.UserFollowRelationshipRepository;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// what do you need:
// user follow update
// register hook event
// when post, publish to all followed
// how receive?
// redis?
// sort by createdTime
// id -> message queue
// keep a 'hot' list(fit size)
// if you want more, turn to db
// consistency?
//
@RestController
@RequestMapping("/users")
public class UserCenterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowRelationshipRepository followRelationshipRepository;

    @Autowired
    private BlogService blogService;

//    @GetMapping("/{id}")
//    public RganUserDTO getUserInfo(@PathVariable Long id){
//        return userRepository.findById(id).map(RganUserDTO::convertFrom)
//            .orElseThrow(() -> new ResourceNotFoundException("user", id));
//    }
    @GetMapping("")
    public RganUserDTO getUserInfo(@RequestParam(value = "name", required = false) String name,  @RequestParam(value = "id", required = false) Long id){
        if (name != null) {
            return userRepository.findByUsername(name).map(RganUserDTO::convertFrom)
                    .orElseThrow(() -> new ResourceNotFoundException("user", name));
        } else {
            return userRepository.findById(id).map(RganUserDTO::convertFrom)
                    .orElseThrow(() -> new ResourceNotFoundException("user", id));
        }
    }

    @PutMapping("/{id}/profile")
    public RganUserDTO editUserProfile(@PathVariable Long id, @RequestBody UserProfileEditParam userProfileEditParam,
                                       @AuthenticationPrincipal RganUser user){
        return RganUserDTO.convertFrom(userRepository.save(user));
    }


    @GetMapping("/{id}/followers")
    public Page<RganUserDTO> getFollowers(@PathVariable("id") Long id, @PageableDefault Pageable pageable){
        RganUser user = checkRequestUserExist(id);
        return followRelationshipRepository
            .findById_BeFollowedAndStatus(user, UserFollowRelationshipStatus.FOLLOWING, pageable)
            .map(followRelationship -> followRelationship.getId().getFollower())
            .map(RganUserDTO::convertFrom);
    }

    @GetMapping("/{id}/following")
    public Page<RganUserDTO> getFollowing(@PathVariable("id") Long id, @PageableDefault Pageable pageable){
        RganUser user = checkRequestUserExist(id);
        return followRelationshipRepository
            .findById_FollowerAndStatusIs(user, UserFollowRelationshipStatus.FOLLOWING, pageable)
            .map(followRelationship -> followRelationship.getId().getBeFollowed())
            .map(RganUserDTO::convertFrom);
    }

    @GetMapping("/self/blogs")
    public Page<BlogSummaryDTO> getSelfBlogs(@RequestParam(value = "status", required = false) Integer status, @AuthenticationPrincipal RganUser self, @PageableDefault Pageable pageable){
        if(status == null){
            return blogService.getAuthorAllBlogsOrderByCreatedTimeDesc(self, pageable);
        }

        return blogService.getAuthorSpecificStatusBlogsOrderByCreatedTimeDesc(self, BlogStatus.selectByValue(status), pageable);
    }


//    @GetMapping("/{id}/followers/size")
//    public Integer getFollowersSize(@PathVariable("id") Long id, @PageableDefault Pageable pageable){
//        RganUser user = checkRequestUserExist(id);
//    }

    private RganUser checkRequestUserExist(@NonNull Long id){
        return userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("user", id)
        );
    }
}
