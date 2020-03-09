package com.okatu.rgan.feed.model.entity;

import javax.persistence.*;

//@Entity
public class MessageBox {
    public static final int BLOG = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverId;

    private Long messageId;

    @Column(nullable = false)
    private Integer messageType;

    private Integer status;
}

