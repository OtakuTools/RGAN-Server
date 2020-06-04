package com.okatu.rgan.feed.model;

import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;

import java.time.LocalDateTime;

public abstract class TimelineResultDTO {
    // feed message self id
    private Long id;

    private String authorName;

    private LocalDateTime createdTime;

    private boolean read;

    public boolean isRead() {
        return read;
    }

    public final void setRead(boolean read) {
        this.read = read;
    }

    public Long getId() {
        return id;
    }

    public final void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public final void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public final void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public TimelineResultDTO() {
    }

    public TimelineResultDTO(FeedMessageBoxItem feedMessageBoxItem){
        this.id = feedMessageBoxItem.getId();
        this.read = feedMessageBoxItem.isRead();
        this.authorName = feedMessageBoxItem.getAuthor().getUsername();
        this.createdTime = feedMessageBoxItem.getCreatedTime();
    }
}
