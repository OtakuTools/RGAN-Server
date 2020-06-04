package com.okatu.rgan.feed.model.param;

import java.util.Set;

public class TimelineMessageReadStatusUpdateParam {
    private Set<Long> messageIds;

    public Set<Long> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(Set<Long> messageIds) {
        this.messageIds = messageIds;
    }
}
