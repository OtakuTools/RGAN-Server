package com.okatu.rgan.blog.model.event;

import org.springframework.context.ApplicationEvent;

public class CommentDeleteEvent extends ApplicationEvent {
    private final long commentId;

    public CommentDeleteEvent(Object source, long commentId) {
        super(source);
        this.commentId = commentId;
    }

    public long getCommentId() {
        return commentId;
    }
}
