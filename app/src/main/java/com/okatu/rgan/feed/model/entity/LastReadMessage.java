package com.okatu.rgan.feed.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity
public class LastReadMessage {
    @Id
    private Long userId;

    private Long lastReadMessageId;
}
