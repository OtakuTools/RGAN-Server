package com.okatu.rgan.vote.model.event;

import com.okatu.rgan.vote.model.entity.VoteItem;
import org.springframework.context.ApplicationEvent;

public class VotePublishEvent extends ApplicationEvent {
    private final VoteItem voteItem;

    public VotePublishEvent(Object source, VoteItem voteItem) {
        super(source);
        this.voteItem = voteItem;
    }

    public VoteItem getVoteItem() {
        return voteItem;
    }
}
