package com.okatu.rgan.blog.model.projection;

import com.okatu.rgan.blog.constant.BlogStatus;
import com.okatu.rgan.blog.constant.BlogType;

import java.time.LocalDateTime;
import java.util.Set;

// class-based cannot use nested projection, see: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections.dtos
public interface BlogSummaryProjection {
    Long getId();

    String getTitle();

    String getSummary();

    BlogType getType();

    BlogStatus getStatus();

    VoteCounter getVoteCounter();

    Integer getVisitorCount();

    AuthorSummary getAuthor();

    // pretty strange here, cannot use class-based projection
    Set<TagSummaryProjection> getTags();

    LocalDateTime getCreatedTime();

    LocalDateTime getModifiedTime();

    interface AuthorSummary {
        String getUsername();

        String getProfilePicturePath();
    }

    interface VoteCounter {
        Integer getValue();
    }
}
