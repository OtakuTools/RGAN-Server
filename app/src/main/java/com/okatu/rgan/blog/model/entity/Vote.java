package com.okatu.rgan.blog.model.entity;

import com.okatu.rgan.user.model.RganUser;

import javax.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private RganUser author;

    private Long targetEntityId;

    // comment, blog or ...
    private Integer type;

    // up or down
    private Integer status;
}
