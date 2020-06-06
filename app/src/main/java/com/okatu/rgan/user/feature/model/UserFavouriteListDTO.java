package com.okatu.rgan.user.feature.model;

import com.okatu.rgan.blog.model.BlogSummaryDTO;

import java.time.LocalDateTime;

public class UserFavouriteListDTO {
    private LocalDateTime enlistTime;

    private BlogSummaryDTO blogSummaryDTO;

    public LocalDateTime getEnlistTime() {
        return enlistTime;
    }

    public void setEnlistTime(LocalDateTime enlistTime) {
        this.enlistTime = enlistTime;
    }

    public BlogSummaryDTO getBlogSummaryDTO() {
        return blogSummaryDTO;
    }

    public void setBlogSummaryDTO(BlogSummaryDTO blogSummaryDTO) {
        this.blogSummaryDTO = blogSummaryDTO;
    }
}
