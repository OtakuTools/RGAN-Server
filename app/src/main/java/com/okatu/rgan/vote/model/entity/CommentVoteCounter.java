package com.okatu.rgan.vote.model.entity;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.common.model.ManuallyAssignIdEntitySuperClass;
import com.okatu.rgan.vote.constant.VoteType;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


@Entity
//@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CommentVoteCounter extends ManuallyAssignIdEntitySuperClass<Long> {
    @Id
    private Long id;

    private Integer value = 0;

    public CommentVoteCounter() {
    }

    public CommentVoteCounter(Comment comment) {
        this.id = comment.getId();
    }

    public Long getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }
}
