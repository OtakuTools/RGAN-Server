package com.okatu.rgan.feed.service;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.model.event.CommentPublishEvent;
import com.okatu.rgan.feed.constant.FeedMessageType;
import com.okatu.rgan.feed.model.entity.FeedMessage;
import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;
import com.okatu.rgan.feed.repository.FeedMessageBoxRepository;
import com.okatu.rgan.feed.repository.FeedMessageRepository;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class FeedMessagePublishService {

    @Autowired
    private FeedMessageRepository feedMessageRepository;

    @Autowired
    private FeedMessageBoxRepository feedMessageBoxRepository;

    @Async
    @EventListener
    public void processCommentPublishEvent(CommentPublishEvent commentPublishEvent){
        // create a new message
        // write to message box
        // if online, notify
        Comment comment = commentPublishEvent.getComment();

        FeedMessage feedMessage = new FeedMessage();
        feedMessage.setAuthor(comment.getAuthor());
        feedMessage.setCreatedTime(comment.getCreatedTime());
        feedMessage.setEntityId(comment.getId());
        feedMessage.setType(FeedMessageType.COMMENT);
        FeedMessage savedFeedMessage = feedMessageRepository.save(feedMessage);

        if(!RganUser.isSame(comment.getAuthor(), comment.getBlog().getAuthor())){
            FeedMessageBoxItem commentAuthorMessageBoxItem = new FeedMessageBoxItem();
            commentAuthorMessageBoxItem.setFeedMessage(savedFeedMessage);
            commentAuthorMessageBoxItem.setMessageType(FeedMessageType.COMMENT);
            // the author of blog which the comment belongs to
            commentAuthorMessageBoxItem.setReceiver(comment.getBlog().getAuthor());
            feedMessageBoxRepository.save(commentAuthorMessageBoxItem);
        }

        if(comment.getReplyTo() != null){
            FeedMessageBoxItem replyToUserMessageBoxItem = new FeedMessageBoxItem();
            replyToUserMessageBoxItem.setFeedMessage(savedFeedMessage);
            replyToUserMessageBoxItem.setMessageType(FeedMessageType.COMMENT);
            replyToUserMessageBoxItem.setReceiver(comment.getReplyTo().getAuthor());
            feedMessageBoxRepository.save(replyToUserMessageBoxItem);
        }
    }
}
