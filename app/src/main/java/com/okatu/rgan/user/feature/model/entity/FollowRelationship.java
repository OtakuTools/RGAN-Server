package com.okatu.rgan.user.feature.model.entity;

import javax.persistence.*;

@Entity
public class FollowRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long beFollowedId;

    private Long followerId;

    @Column(nullable = false)
    private Integer type;

    @Column(nullable = false)
    private Integer status;

    public Long getBeFollowedId() {
        return beFollowedId;
    }

    public void setBeFollowedId(Long beFollowedId) {
        this.beFollowedId = beFollowedId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
