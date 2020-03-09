package com.okatu.rgan.feed.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "feed_message")
public class Message {
    private Long id;

    private Long entityId;

    private Long authorId;

    private Integer type;

    private Integer status;

    private LocalDateTime createdTime;
}
