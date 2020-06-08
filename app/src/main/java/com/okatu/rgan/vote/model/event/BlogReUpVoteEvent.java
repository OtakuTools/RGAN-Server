package com.okatu.rgan.vote.model.event;

import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import org.springframework.context.ApplicationEvent;

public class BlogReUpVoteEvent extends ApplicationEvent {
    private final BlogVoteItem voteItem;

    public BlogReUpVoteEvent(Object source, BlogVoteItem voteItem) {
        super(source);
        this.voteItem = voteItem;
    }

    public BlogVoteItem getVoteItem() {
        return voteItem;
    }
}
