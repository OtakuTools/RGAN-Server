package com.okatu.rgan.feed.model.entity;

import javax.persistence.*;

//@Entity
public class FollowRelationship {
    public final static int USER = 1;
    public final static int TAG = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long beFollowedId;

    private Long followerId;

    @Column(nullable = false)
    private Integer type;

    private Integer status;

}
