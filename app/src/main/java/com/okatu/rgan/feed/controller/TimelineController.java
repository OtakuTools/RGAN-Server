package com.okatu.rgan.feed.controller;

import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.repository.CommentRepository;
import com.okatu.rgan.blog.service.BlogService;
import com.okatu.rgan.feed.constant.FeedMessageStatus;
import com.okatu.rgan.feed.constant.FeedMessageType;
import com.okatu.rgan.feed.model.dto.TimelineCommentDTO;
import com.okatu.rgan.feed.model.dto.TimelineMessageUnreadNumberDTO;
import com.okatu.rgan.feed.model.dto.TimelineUpVoteDTO;
import com.okatu.rgan.feed.model.param.TimelineMessageReadStatusUpdateParam;
import com.okatu.rgan.feed.repository.FeedMessageBoxRepository;
import com.okatu.rgan.feed.service.TimelineService;
import com.okatu.rgan.user.feature.constant.UserFollowRelationshipStatus;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.UserFollowRelationshipRepository;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private TimelineService timelineService;

    @GetMapping("/news")
    public TimelineMessageUnreadNumberDTO checkNewTimelineMessage(@AuthenticationPrincipal RganUser user){
        TimelineMessageUnreadNumberDTO unreadNumberDTO = new TimelineMessageUnreadNumberDTO();
        unreadNumberDTO.setCommentNum(feedMessageBoxRepository.countByReceiverAndMessageTypeAndMessageStatusAndReadIsFalse(user, FeedMessageType.COMMENT, FeedMessageStatus.ENABLED));
        unreadNumberDTO.setUpVoteNum(feedMessageBoxRepository.countUnreadVoteItemByReceiverAndMessageStatus(user, FeedMessageStatus.ENABLED));
        return unreadNumberDTO;
    }

    @PostMapping("/read")
    public void updateTimelineMessageReadStatus(@RequestBody TimelineMessageReadStatusUpdateParam param, @AuthenticationPrincipal RganUser user){

        feedMessageBoxRepository.saveAll(
            feedMessageBoxRepository.findAllByIdInAndReceiver(param.getMessageIds(), user)
                .stream().peek(feedMessageBoxItem -> feedMessageBoxItem.setRead(true)).collect(Collectors.toList())
        );
    }

    @GetMapping("/blogs")
    public Page<BlogSummaryDTO> getTimeline(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        Set<RganUser> followingUsers = userFollowRelationshipRepository.findById_FollowerAndStatus(user, UserFollowRelationshipStatus.FOLLOWING)
            .stream().map(userFollowRelationship -> userFollowRelationship.getId().getBeFollowed()).collect(Collectors.toSet());

        return blogService.getAuthorsPublishedBlogsOrderByCreatedTimeDesc(followingUsers, pageable);
    }

    @GetMapping("/comments")
    public Page<TimelineCommentDTO> getReplyComment(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        return timelineService.getTimelineCommentByReceiver(user, pageable);
    }

    @GetMapping("/votes")
    public Page<TimelineUpVoteDTO> get(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        return timelineService.getTimelineUpVoteByReceiver(user, pageable);

        // who vote you for... ?
        // you have two options
        // feedMessage add an... or feedMessageBox?
        // when will a feedMessage exist in multiple MessageBox?
        // except for blog?

        // two options here,
        // withdraw strategy on FeedMessage or MessageBox side(the property that indicate withdraw should exist on which entity)
        // FeedMessage pros and cons:
        // 1. less space, don't need to replicate it every messageBox
        // 2. however, what if some special logic, that a published message(comment/vote) can be seen by specific user?
        // does this kind of requirement exist?
        // if we don't need this, that a message withdraw can be done with only one time db write without change every MessageBox status
        // but now, for vote/comment, only one or two MessageBox
        // 3. paging might be difficult, since we retrieve the message like MessageBox -> FeedMessage -> Concrete(vote/comment)
    }

    // if you need to notify user when they login that they have new unread message, need an interface
    // so that the front end can request it after login
    // so some storage, to tell whether a message is unread or not
    //
}
