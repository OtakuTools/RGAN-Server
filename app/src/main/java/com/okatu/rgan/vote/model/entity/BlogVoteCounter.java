package com.okatu.rgan.vote.model.entity;

import com.okatu.rgan.blog.model.entity.Blog;

import javax.persistence.*;

@Entity
public class BlogVoteCounter {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "id")
    @MapsId
    private Blog blog;

    private Integer value = 0;

    public BlogVoteCounter() {
    }

    public BlogVoteCounter(Blog blog) {
        this.blog = blog;
    }

    public Blog getBlog() {
        return blog;
    }

    public Integer getValue() {
        return value;
    }
}
