package com.okatu.rgan.feed.controller;

import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.model.CommentSummaryDTO;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.blog.repository.CommentRepository;
import com.okatu.rgan.blog.service.BlogService;
import com.okatu.rgan.feed.constant.FeedMessageType;
import com.okatu.rgan.feed.model.TimelineCommentDTO;
import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;
import com.okatu.rgan.feed.repository.FeedMessageBoxRepository;
import com.okatu.rgan.user.feature.constant.UserFollowRelationshipStatus;
import com.okatu.rgan.user.feature.model.entity.UserFollowRelationship;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.UserFollowRelationshipRepository;
import com.okatu.rgan.user.repository.UserRepository;
import com.okatu.rgan.vote.model.VoteStatusDTO;
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
    private UserRepository userRepository;

    @Autowired
    private FeedMessageBoxRepository feedMessageBoxRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogService blogService;

    @GetMapping("/blogs")
    public Page<BlogSummaryDTO> getTimeline(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        Set<RganUser> followingUsers = userFollowRelationshipRepository.findById_FollowerAndStatus(user, UserFollowRelationshipStatus.FOLLOWING)
            .stream().map(userFollowRelationship -> userFollowRelationship.getId().getBeFollowed()).collect(Collectors.toSet());

        return blogService.getAuthorsPublishedBlogsOrderByCreatedTimeDesc(followingUsers, pageable);
    }

    @GetMapping("/comments")
    public Page<TimelineCommentDTO> getReplyComment(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        return feedMessageBoxRepository
            .findByReceiverAndMessageTypeOrderByFeedMessageDesc(user, FeedMessageType.COMMENT, pageable)
            .map(feedMessageBoxItem -> commentRepository.findById(feedMessageBoxItem.getFeedMessage().getEntityId()).get())
            .map(TimelineCommentDTO::convertFrom);
    }

    @GetMapping("/votes")
    public Page<VoteStatusDTO> get(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        Page<FeedMessageBoxItem> items = feedMessageBoxRepository
            .findByReceiverAndMessageTypeOrderByFeedMessageDesc(user, FeedMessageType.VOTE, pageable);

        // who vote you for... ?
        // you have two options
        // feedMessage add an... or feedMessageBox?
        // when will a feedMessage exist in multiple MessageBox?
        // except for blog?
        //
        return null;
    }
}