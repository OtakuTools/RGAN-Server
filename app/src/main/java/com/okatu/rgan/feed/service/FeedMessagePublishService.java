package com.okatu.rgan.feed.service;

import com.okatu.rgan.blog.model.event.CommentPublishEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class FeedMessagePublishService {
    @Async
    @EventListener
    public void processCommentPublishEvent(CommentPublishEvent commentPublishEvent){
        // create a new message
        // write to message box
        // if online, notify
    }
}
