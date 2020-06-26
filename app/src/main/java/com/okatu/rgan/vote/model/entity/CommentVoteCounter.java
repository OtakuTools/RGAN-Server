package com.okatu.rgan.vote.model.entity;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.vote.constant.VoteType;

import javax.persistence.*;

@Entity
public class CommentVoteCounter {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "id")
    @MapsId
    private Comment comment;

    private Integer value = 0;

    public CommentVoteCounter() {
    }

    public CommentVoteCounter(Comment comment) {
        this.comment = comment;
    }


    public Comment getComment() {
        return comment;
    }

    public Integer getValue() {
        return value;
    }
}
