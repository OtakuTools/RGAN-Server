package com.okatu.rgan.user.feature.model;

import com.okatu.rgan.blog.model.BlogSummaryDTO;

import java.time.LocalDateTime;

public class UserFavouriteListDTO {
    private LocalDateTime enlistTime;

    private BlogSummaryDTO blogSummary;

    public LocalDateTime getEnlistTime() {
        return enlistTime;
    }

    public void setEnlistTime(LocalDateTime enlistTime) {
        this.enlistTime = enlistTime;
    }

    public BlogSummaryDTO getBlogSummary() {
        return blogSummary;
    }

    public void setBlogSummary(BlogSummaryDTO blogSummary) {
        this.blogSummary = blogSummary;
    }
}
