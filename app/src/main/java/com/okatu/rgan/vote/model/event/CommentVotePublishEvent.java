package com.okatu.rgan.vote.model.event;

import com.okatu.rgan.vote.model.entity.CommentVoteItem;
import org.springframework.context.ApplicationEvent;

public class CommentVotePublishEvent extends ApplicationEvent {
    private final CommentVoteItem voteItem;

    public CommentVotePublishEvent(Object source, CommentVoteItem voteItem) {
        super(source);
        this.voteItem = voteItem;
    }

    public CommentVoteItem getVoteItem() {
        return voteItem;
    }
}
