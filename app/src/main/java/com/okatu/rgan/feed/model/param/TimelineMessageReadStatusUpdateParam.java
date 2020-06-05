package com.okatu.rgan.feed.model.param;

import java.util.Set;

public class TimelineMessageReadStatusUpdateParam {
    private Set<Long> messageItemIds;

    public Set<Long> getMessageItemIds() {
        return messageItemIds;
    }

    public void setMessageItemIds(Set<Long> messageItemIds) {
        this.messageItemIds = messageItemIds;
    }
}
