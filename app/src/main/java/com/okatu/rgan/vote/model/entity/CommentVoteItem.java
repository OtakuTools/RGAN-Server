package com.okatu.rgan.vote.model.entity;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.constant.VoteType;
import com.okatu.rgan.vote.model.VoteAbleEntity;

import javax.persistence.*;

@Entity
@DiscriminatorValue(VoteType.COMMENT_DISCRIMINATOR)
public class CommentVoteItem extends VoteItem{
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private Comment comment;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public VoteAbleEntity getAssociateVoteAbleEntity() {
        return comment;
    }

    public CommentVoteItem() {
    }

    public CommentVoteItem(RganUser author, Comment comment) {
        super(author);
        this.comment = comment;
    }
}
