package com.okatu.rgan.feed.model.entity;

import javax.persistence.*;

//@Entity
//@Table(name = "feed_message_box")
public class MessageBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverId;

    private Long messageId;

    @Column(nullable = false)
    private Integer messageType;

    private Integer status;
}

