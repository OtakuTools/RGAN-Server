package com.okatu.rgan.feed.model.entity;

import com.okatu.rgan.feed.constant.FeedMessageStatus;
import com.okatu.rgan.feed.constant.FeedMessageType;
import com.okatu.rgan.user.model.RganUser;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

// 对于撤回逻辑，改动这个消息队列的中间项是好主意吗？
// 还是说只追加撤回事件？
// 但是这个模型实际上不是消息队列的抽象，而是一个用户维度的inbox
// 目前系统中是没有一条全局的feed消息队列抽象的
@Entity
@Table(name = "feed_message_box",
    uniqueConstraints = {
    @UniqueConstraint(columnNames = {"message_id", "message_type", "receiver_id"})
})
public class FeedMessageBoxItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private RganUser receiver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private RganUser author;

    @Column(nullable = false, name = "message_id")
    private Long messageId;

    @Column(nullable = false, name = "message_type")
    @Convert(converter = FeedMessageType.Converter.class)
    private FeedMessageType messageType;

    @Column(nullable = false)
    @Convert(converter = FeedMessageStatus.Converter.class)
    private FeedMessageStatus messageStatus = FeedMessageStatus.ENABLED;

    @Column(nullable = false, name = "is_read")
    private boolean read = false;

    private LocalDateTime createdTime;

    public Long getId() {
        return id;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public RganUser getReceiver() {
        return receiver;
    }

    public void setReceiver(RganUser receiver) {
        this.receiver = receiver;
    }

    public RganUser getAuthor() {
        return author;
    }

    public void setAuthor(RganUser author) {
        this.author = author;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public FeedMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(FeedMessageType messageType) {
        this.messageType = messageType;
    }

    public FeedMessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(FeedMessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}

