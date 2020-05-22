package com.okatu.rgan.vote.model.event;

import com.okatu.rgan.vote.model.entity.VoteItem;
import org.springframework.context.ApplicationEvent;

public class VotePublishEvent<T extends VoteItem> extends ApplicationEvent {
    private final T voteItem;

    public VotePublishEvent(Object source, T voteItem) {
        super(source);
        this.voteItem = voteItem;
    }

    public T getVoteItem() {
        return voteItem;
    }
}
