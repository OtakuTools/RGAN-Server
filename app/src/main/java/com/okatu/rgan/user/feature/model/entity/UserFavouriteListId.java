package com.okatu.rgan.user.feature.model.entity;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.user.model.RganUser;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserFavouriteListId implements Serializable {
    @ManyToOne(optional = false)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private RganUser user;

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public RganUser getUser() {
        return user;
    }

    public void setUser(RganUser user) {
        this.user = user;
    }

    public UserFavouriteListId() {
    }

    public UserFavouriteListId(Blog blog, RganUser user) {
        this.blog = blog;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFavouriteListId)) return false;
        UserFavouriteListId that = (UserFavouriteListId) o;
        return Objects.equals(blog, that.blog) &&
            Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blog, user);
    }
}
