package com.okatu.rgan.feed.service;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.model.event.CommentPublishEvent;
import com.okatu.rgan.feed.SseNotificationService;
import com.okatu.rgan.feed.constant.FeedMessageStatus;
import com.okatu.rgan.feed.constant.FeedMessageType;
import com.okatu.rgan.feed.model.dto.TimelineCommentDTO;
import com.okatu.rgan.feed.model.dto.TimelineUpVoteDTO;
import com.okatu.rgan.feed.model.dto.VoteWithdrawDTO;
import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;
import com.okatu.rgan.feed.repository.FeedMessageBoxRepository;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import com.okatu.rgan.vote.model.entity.CommentVoteItem;
import com.okatu.rgan.vote.model.event.BlogVotePublishEvent;
import com.okatu.rgan.vote.model.event.CommentUpVoteCancelEvent;
import com.okatu.rgan.vote.model.event.CommentVotePublishEvent;
import com.okatu.rgan.vote.model.event.BlogUpVoteCancelEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class FeedMessageRelatedEventHandleService {

    private static String COMMENT_NEW_NOTIFICATION_EVENT_NAME = "comment_new";

    private static String COMMENT_VOTE_NOTIFICATION_EVENT_NAME = "comment_vote";

    private static String BLOG_VOTE_NOTIFICATION_EVENT_NAME = "blog_vote";

    private static String BLOG_VOTE_WITHDRAW_NOTIFICATION_EVENT_NAME = "blog_vote_withdraw";

    private static String COMMENT_VOTE_WITHDRAW_NOTIFICATION_EVENT_NAME = "comment_vote_withdraw";

    @Autowired
    private FeedMessageBoxRepository feedMessageBoxRepository;

    @Autowired
    private SseNotificationService sseNotificationService;

    @Async
    @EventListener
    public void processCommentPublishEvent(CommentPublishEvent commentPublishEvent){
        // create a new message
        // write to message box
        // if online, notify
        Comment comment = commentPublishEvent.getComment();

        RganUser blogAuthor = comment.getBlog().getAuthor();
        if(RganUser.isNotSame(comment.getAuthor(), blogAuthor)){
            FeedMessageBoxItem blogAuthorMessageBoxItem = new FeedMessageBoxItem();
            blogAuthorMessageBoxItem.setAuthor(comment.getAuthor());
            blogAuthorMessageBoxItem.setCreatedTime(comment.getCreatedTime());
            blogAuthorMessageBoxItem.setMessageId(comment.getId());
            blogAuthorMessageBoxItem.setMessageType(FeedMessageType.COMMENT);
            // the author of blog which the comment belongs to
            blogAuthorMessageBoxItem.setReceiver(blogAuthor);

            feedMessageBoxRepository.save(blogAuthorMessageBoxItem);

            sseNotificationService.sendMessage(blogAuthor, COMMENT_NEW_NOTIFICATION_EVENT_NAME,
                TimelineCommentDTO.createFrom(blogAuthorMessageBoxItem, comment));
        }

        if(comment.getReplyTo() != null){
            FeedMessageBoxItem replyToUserMessageBoxItem = new FeedMessageBoxItem();
            RganUser receiver = comment.getReplyTo().getAuthor();
            replyToUserMessageBoxItem.setAuthor(comment.getAuthor());
            replyToUserMessageBoxItem.setCreatedTime(comment.getCreatedTime());
            replyToUserMessageBoxItem.setMessageId(comment.getId());
            replyToUserMessageBoxItem.setMessageType(FeedMessageType.COMMENT);
            replyToUserMessageBoxItem.setReceiver(receiver);

            feedMessageBoxRepository.save(replyToUserMessageBoxItem);

            sseNotificationService.sendMessage(receiver, COMMENT_NEW_NOTIFICATION_EVENT_NAME,
                TimelineCommentDTO.createFrom(replyToUserMessageBoxItem, comment));
        }


    }

    @Async
    @EventListener
    public void processCommentVotePublishEvent(CommentVotePublishEvent commentVotePublishEvent){
        FeedMessageBoxItem messageBoxItem = new FeedMessageBoxItem();
        CommentVoteItem commentVoteItem = commentVotePublishEvent.getVoteItem();
        RganUser receiver = commentVoteItem.getComment().getAuthor();
        messageBoxItem.setReceiver(receiver);
        messageBoxItem.setAuthor(commentVoteItem.getAuthor());
        messageBoxItem.setMessageId(commentVoteItem.getId());
        messageBoxItem.setMessageType(FeedMessageType.COMMENT_VOTE);
        messageBoxItem.setCreatedTime(commentVoteItem.getCreatedTime());

        feedMessageBoxRepository.save(messageBoxItem);

        sseNotificationService.sendMessage(receiver, COMMENT_VOTE_NOTIFICATION_EVENT_NAME,
            TimelineUpVoteDTO.createFrom(messageBoxItem, commentVoteItem));
    }

    @Async
    @EventListener
    public void processBlogVotePublishEvent(BlogVotePublishEvent blogVotePublishEvent){
        FeedMessageBoxItem messageBoxItem = new FeedMessageBoxItem();
        BlogVoteItem blogVoteItem = blogVotePublishEvent.getVoteItem();
        RganUser receiver = blogVoteItem.getBlog().getAuthor();
        messageBoxItem.setReceiver(receiver);
        messageBoxItem.setAuthor(blogVoteItem.getAuthor());
        messageBoxItem.setMessageId(blogVoteItem.getId());
        messageBoxItem.setMessageType(FeedMessageType.BLOG_VOTE);
        messageBoxItem.setCreatedTime(blogVoteItem.getCreatedTime());

        feedMessageBoxRepository.save(messageBoxItem);

        sseNotificationService.sendMessage(receiver, BLOG_VOTE_NOTIFICATION_EVENT_NAME,
            TimelineUpVoteDTO.createFrom(messageBoxItem, blogVoteItem));
    }

    @Async
    @EventListener
    public void processBlogUpVoteCancelEvent(BlogUpVoteCancelEvent blogUpVoteCancelEvent){
        feedMessageBoxRepository.findByMessageIdAndMessageType(blogUpVoteCancelEvent.getVoteItem().getId(), FeedMessageType.BLOG_VOTE)
            .ifPresent(feedMessageBoxItem -> {
                feedMessageBoxItem.setMessageStatus(FeedMessageStatus.DELETED);
                feedMessageBoxRepository.save(feedMessageBoxItem);

                sseNotificationService.sendMessage(feedMessageBoxItem.getReceiver(), BLOG_VOTE_WITHDRAW_NOTIFICATION_EVENT_NAME,
                    new VoteWithdrawDTO(feedMessageBoxItem.getId()));
            });
    }

    @Async
    @EventListener
    public void processCommentUpVoteCancelEvent(CommentUpVoteCancelEvent commentUpVoteCancelEvent){
        feedMessageBoxRepository.findByMessageIdAndMessageType(commentUpVoteCancelEvent.getVoteItem().getId(), FeedMessageType.COMMENT_VOTE)
            .ifPresent(feedMessageBoxItem -> {
                feedMessageBoxItem.setMessageStatus(FeedMessageStatus.DELETED);
                feedMessageBoxRepository.save(feedMessageBoxItem);

                sseNotificationService.sendMessage(feedMessageBoxItem.getReceiver(), COMMENT_VOTE_WITHDRAW_NOTIFICATION_EVENT_NAME,
                    new VoteWithdrawDTO(feedMessageBoxItem.getId()));
            });
    }
}