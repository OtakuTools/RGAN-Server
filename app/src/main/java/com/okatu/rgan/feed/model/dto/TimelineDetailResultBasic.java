package com.okatu.rgan.feed.model.dto;

import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;
import com.okatu.rgan.user.model.AuthorInCreateEntityDTO;

import java.time.LocalDateTime;

public abstract class TimelineDetailResultBasic {
    // feed message self id
    private Long id;

    private AuthorInCreateEntityDTO author;

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

    public AuthorInCreateEntityDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInCreateEntityDTO author) {
        this.author = author;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public final void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public TimelineDetailResultBasic() {
    }

    public TimelineDetailResultBasic(FeedMessageBoxItem feedMessageBoxItem){
        this.id = feedMessageBoxItem.getId();
        this.read = feedMessageBoxItem.isRead();
        this.author = new AuthorInCreateEntityDTO(feedMessageBoxItem.getAuthor().getUsername(), feedMessageBoxItem.getAuthor().getProfilePicturePath());
        this.createdTime = feedMessageBoxItem.getCreatedTime();
    }
}
