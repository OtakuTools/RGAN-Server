package com.okatu.rgan.blog.model.projection;

import com.okatu.rgan.blog.model.entity.Tag;
import com.okatu.rgan.user.model.RganUser;

import java.time.LocalDateTime;
import java.util.Set;

// class-based cannot use nested projection, see: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections.dtos
public interface BlogSummaryProjection {
    Long getId();

    String getTitle();

    String getSummary();

    Integer getUpvoteCount();

    Integer getVisitorCount();

    UserSummary getUser();

    Set<Tag> getTags();

    LocalDateTime getCreatedTime();

    LocalDateTime getModifiedTime();

    interface UserSummary{
        String getUsername();
    }
}
