package com.okatu.rgan.feed.model.entity;

import com.okatu.rgan.user.model.RganUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feed_message")
public class FeedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long entityId;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private RganUser author;

    private Integer type;

    private LocalDateTime createdTime;

    public Long getId() {
        return id;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public RganUser getAuthor() {
        return author;
    }

    public void setAuthor(RganUser author) {
        this.author = author;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
