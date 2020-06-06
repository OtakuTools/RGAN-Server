package com.okatu.rgan.user.feature.model.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

// why don't use join table mapping
// see the comment in Blog class
// we need some extra columns
@Entity
public class UserFavouriteList {

    @EmbeddedId
    private UserFavouriteListId id;

    private LocalDateTime createdTime;

    @Column(nullable = false)
    private boolean enabled = true;

    public UserFavouriteListId getId() {
        return id;
    }

    public void setId(UserFavouriteListId id) {
        this.id = id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @PrePersist
    private void prePersist(){
        createdTime = LocalDateTime.now();
    }

    public UserFavouriteList() {
    }

    public UserFavouriteList(UserFavouriteListId id) {
        this.id = id;
    }
}
