package com.okatu.rgan.feed.service;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.repository.CommentRepository;
import com.okatu.rgan.common.exception.ConstraintViolationException;
import com.okatu.rgan.common.exception.ResourceAccessDeniedException;
import com.okatu.rgan.feed.constant.FeedMessageStatus;
import com.okatu.rgan.feed.constant.FeedMessageType;
import com.okatu.rgan.feed.model.dto.TimelineCommentDTO;
import com.okatu.rgan.feed.model.dto.TimelineUpVoteDTO;
import com.okatu.rgan.feed.repository.FeedMessageBoxRepository;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import com.okatu.rgan.vote.model.entity.CommentVoteItem;
import com.okatu.rgan.vote.repository.BlogVoteItemRepository;
import com.okatu.rgan.vote.repository.CommentVoteItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TimelineService {

    @Autowired
    private FeedMessageBoxRepository feedMessageBoxRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentVoteItemRepository commentVoteItemRepository;

    @Autowired
    private BlogVoteItemRepository blogVoteItemRepository;

    public Page<TimelineCommentDTO> getTimelineCommentByReceiver(@NonNull RganUser user, Pageable pageable){
        return feedMessageBoxRepository.findByReceiverAndMessageTypeAndMessageStatusOrderByCreatedTimeDesc(
            user, FeedMessageType.COMMENT, FeedMessageStatus.ENABLED, pageable)
            .map(feedMessageBoxItem -> {
                Optional<Comment> optional = commentRepository.findById(feedMessageBoxItem.getMessageId());
                // comment might be deleted
                return optional.map(comment -> TimelineCommentDTO.createFrom(feedMessageBoxItem, comment)).orElse(null);
            });
    }

    public Page<TimelineUpVoteDTO> getTimelineUpVoteByReceiver(@NonNull RganUser user, Pageable pageable){
        return feedMessageBoxRepository.findAllVoteItemByReceiverAndMessageStatusOrderByCreatedTimeDesc(user, FeedMessageStatus.ENABLED, pageable)
            .map(feedMessageBoxItem -> {
                TimelineUpVoteDTO timelineUpVoteDTO;
                if(FeedMessageType.BLOG_VOTE.equals(feedMessageBoxItem.getMessageType())){
                    BlogVoteItem blogVoteItem = blogVoteItemRepository.findById(feedMessageBoxItem.getMessageId()).get();

                    timelineUpVoteDTO = TimelineUpVoteDTO.createFrom(feedMessageBoxItem, blogVoteItem);
                }else if(FeedMessageType.COMMENT_VOTE.equals(feedMessageBoxItem.getMessageType())){
                    CommentVoteItem commentVoteItem = commentVoteItemRepository.findById(feedMessageBoxItem.getMessageId()).get();

                    timelineUpVoteDTO = TimelineUpVoteDTO.createFrom(feedMessageBoxItem, commentVoteItem);
                }else{
                    throw new ConstraintViolationException("should not hit here");
                }

                return timelineUpVoteDTO;
            });
    }

    public void deleteReceiveTimelineMessage(long messageId, @NonNull RganUser self){
        feedMessageBoxRepository.findById(messageId).ifPresent(feedMessageBoxItem -> {
            if(RganUser.isNotSame(feedMessageBoxItem.getReceiver(), self)){
                throw new ResourceAccessDeniedException("You cannot access this resource");
            }
            feedMessageBoxItem.setMessageStatus(FeedMessageStatus.DELETED);
            feedMessageBoxRepository.save(feedMessageBoxItem);
        });
    }
}
