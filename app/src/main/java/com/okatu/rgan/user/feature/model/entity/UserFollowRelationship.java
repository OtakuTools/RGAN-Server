package com.okatu.rgan.user.feature.model.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

// the reason why don't we use @ManyToMany to map the relationship,
// see the comment in class Blog
@Entity
public class UserFollowRelationship {
    @EmbeddedId
    private UserFollowRelationshipId id;

    @Column(nullable = false)
    private Integer status;

    public UserFollowRelationshipId getId() {
        return id;
    }

    public void setId(UserFollowRelationshipId id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UserFollowRelationship() {
    }

    public UserFollowRelationship(UserFollowRelationshipId id, Integer status) {
        this.id = id;
        this.status = status;
    }
}
