package com.okatu.rgan.vote.model.event;

import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import org.springframework.context.ApplicationEvent;

// We can use generic event like VoteEvent<T extends VoteItem>
// and implement some TypeResolver or what
// but seems to have no necessity for now, since we only have two types of event
public class BlogVotePublishEvent extends ApplicationEvent {
    private final BlogVoteItem voteItem;

    public BlogVotePublishEvent(Object source, BlogVoteItem voteItem) {
        super(source);
        this.voteItem = voteItem;
    }

    public BlogVoteItem getVoteItem() {
        return voteItem;
    }
}
