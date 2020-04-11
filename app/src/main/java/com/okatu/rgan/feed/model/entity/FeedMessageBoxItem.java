package com.okatu.rgan.feed.model.entity;

import com.okatu.rgan.user.model.RganUser;

import javax.persistence.*;

@Entity
@Table(name = "feed_message_box")
public class FeedMessageBoxItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private RganUser receiver;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private FeedMessage feedMessage;

    // duplicate type here
    // for example, when pull all the comment, can do this filter on this side,
    // not need to retrieve all the message_box then retrieve the message then do the filter
    @Column(nullable = false)
    private Integer messageType;

    public RganUser getReceiver() {
        return receiver;
    }

    public void setReceiver(RganUser receiver) {
        this.receiver = receiver;
    }

    public FeedMessage getFeedMessage() {
        return feedMessage;
    }

    public void setFeedMessage(FeedMessage feedMessage) {
        this.feedMessage = feedMessage;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
}

