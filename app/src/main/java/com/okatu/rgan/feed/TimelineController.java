package com.okatu.rgan.feed;

import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.model.CommentSummaryDTO;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.blog.repository.CommentRepository;
import com.okatu.rgan.feed.constant.FeedMessageType;
import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;
import com.okatu.rgan.feed.repository.FeedMessageBoxRepository;
import com.okatu.rgan.user.feature.constant.UserFollowRelationshipStatus;
import com.okatu.rgan.user.feature.model.entity.UserFollowRelationship;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.UserFollowRelationshipRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/timeline")
public class TimelineController {

    @Autowired
    private UserFollowRelationshipRepository userFollowRelationshipRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedMessageBoxRepository feedMessageBoxRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/blogs")
    public Page<BlogSummaryDTO> getTimeline(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        Set<RganUser> followingUsers = userFollowRelationshipRepository.findById_FollowerAndStatus(user, UserFollowRelationshipStatus.FOLLOWING)
            .stream().map(userFollowRelationship -> userFollowRelationship.getId().getBeFollowed()).collect(Collectors.toSet());

        return blogRepository.findByAuthorInOrderByCreatedTimeDesc(followingUsers, pageable).map(BlogSummaryDTO::convertFrom);
    }

    @GetMapping("/comments")
    public Page<CommentSummaryDTO> getReplyComment(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        Page<FeedMessageBoxItem> items = feedMessageBoxRepository
            .findByReceiverAndMessageTypeOrderByFeedMessageDesc(user, FeedMessageType.COMMENT, pageable);
        return feedMessageBoxRepository
            .findByReceiverAndMessageTypeOrderByFeedMessageDesc(user, FeedMessageType.COMMENT, pageable)
            .map(feedMessageBoxItem -> commentRepository.findByIdIs(feedMessageBoxItem.getFeedMessage().getEntityId()))
            .map(CommentSummaryDTO::convertFrom);
    }

}
