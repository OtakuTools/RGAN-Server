package com.okatu.rgan.vote.model.event;

import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import com.okatu.rgan.vote.model.entity.VoteItem;
import org.springframework.context.ApplicationEvent;

public class BlogUpVoteCancelEvent extends ApplicationEvent {
    private final BlogVoteItem voteItem;

    public BlogUpVoteCancelEvent(Object source, BlogVoteItem voteItem) {
        super(source);
        this.voteItem = voteItem;
    }

    public BlogVoteItem getVoteItem() {
        return voteItem;
    }
}
