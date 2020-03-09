package com.okatu.rgan.feed;

import com.okatu.rgan.blog.model.BlogDTO;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.user.feature.constant.FollowRelationshipStatus;
import com.okatu.rgan.user.feature.constant.FollowRelationshipType;
import com.okatu.rgan.user.feature.model.entity.FollowRelationship;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.FollowRelationshipRepository;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/timeline")
public class TimelineController {
    @Autowired
    private FollowRelationshipRepository followRelationshipRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Page<BlogDTO> getTimeline(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        List<Long> followingId = followRelationshipRepository
            .findByFollowerIdAndTypeAndStatus(user.getId(), FollowRelationshipType.USER, FollowRelationshipStatus.FOLLOWING)
            .stream().map(FollowRelationship::getBeFollowedId).collect(Collectors.toList());

        List<RganUser> users = userRepository.findAllById(followingId);

        return blogRepository.findByUserInOrderByCreatedTimeDesc(users, pageable).map(BlogDTO::convertFrom);
    }
}
