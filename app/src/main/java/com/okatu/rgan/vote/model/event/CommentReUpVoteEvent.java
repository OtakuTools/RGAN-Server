package com.okatu.rgan.vote.model.event;

import com.okatu.rgan.vote.model.entity.CommentVoteItem;
import org.springframework.context.ApplicationEvent;

public class CommentReUpVoteEvent extends ApplicationEvent {
    private final CommentVoteItem voteItem;

    public CommentReUpVoteEvent(Object source, CommentVoteItem voteItem) {
        super(source);
        this.voteItem = voteItem;
    }

    public CommentVoteItem getVoteItem() {
        return voteItem;
    }
}
