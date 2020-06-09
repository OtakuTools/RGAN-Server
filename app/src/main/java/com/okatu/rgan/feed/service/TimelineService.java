package com.okatu.rgan.feed.service;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.repository.CommentRepository;
import com.okatu.rgan.common.exception.ConstraintViolationException;
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
import org.springframework.stereotype.Component;

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

    public Page<TimelineCommentDTO> getTimelineCommentByReceiver(RganUser user, Pageable pageable){
        return feedMessageBoxRepository.findByReceiverAndMessageTypeAndMessageStatusOrderByCreatedTimeDesc(
            user, FeedMessageType.COMMENT, FeedMessageStatus.ENABLED, pageable)
            .map(feedMessageBoxItem -> {
                Comment comment = commentRepository.findById(feedMessageBoxItem.getMessageId()).get();
                return TimelineCommentDTO.createFrom(feedMessageBoxItem, comment);
            });
    }

    public Page<TimelineUpVoteDTO> getTimelineUpVoteByReceiver(RganUser user, Pageable pageable){
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
}
