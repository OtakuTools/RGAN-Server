package com.okatu.rgan.user.feature.model.entity;

import com.okatu.rgan.user.model.RganUser;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserFollowRelationshipId implements Serializable {

    @ManyToOne(optional = false)
    @JoinColumn(name = "be_followed_id", nullable = false, updatable = false)
    private RganUser beFollowed;

    @ManyToOne(optional = false)
    @JoinColumn(name = "follower_id", nullable = false, updatable = false)
    private RganUser follower;

    public UserFollowRelationshipId() {
    }

    public UserFollowRelationshipId(RganUser beFollowed, RganUser follower) {
        this.beFollowed = beFollowed;
        this.follower = follower;
    }

    public RganUser getBeFollowed() {
        return beFollowed;
    }

    public void setBeFollowed(RganUser beFollowed) {
        this.beFollowed = beFollowed;
    }

    public RganUser getFollower() {
        return follower;
    }

    public void setFollower(RganUser follower) {
        this.follower = follower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFollowRelationshipId)) return false;
        UserFollowRelationshipId that = (UserFollowRelationshipId) o;
        return Objects.equals(beFollowed, that.beFollowed) &&
            Objects.equals(follower, that.follower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beFollowed, follower);
    }
}
