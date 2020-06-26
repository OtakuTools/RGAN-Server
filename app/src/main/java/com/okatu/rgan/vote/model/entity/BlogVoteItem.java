package com.okatu.rgan.vote.model.entity;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.constant.VoteType;

import javax.persistence.*;

@Entity
@DiscriminatorValue(VoteType.BLOG_DISCRIMINATOR)
public class BlogVoteItem extends VoteItem{

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "entity_id", nullable = false, updatable = false)
    private Blog blog;

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public BlogVoteItem(RganUser author, Blog blog) {
        super(author);
        this.blog = blog;
    }

    public BlogVoteItem() {
        super();
    }
}
