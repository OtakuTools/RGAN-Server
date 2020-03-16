package com.okatu.rgan.blog.model.event;

import com.okatu.rgan.blog.model.entity.Comment;
import org.springframework.context.ApplicationEvent;

public class CommentPublishEvent extends ApplicationEvent {
    private final Comment comment;

    public CommentPublishEvent(Object source, Comment comment) {
        super(source);
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
