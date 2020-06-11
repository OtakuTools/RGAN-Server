package com.okatu.rgan.blog.model.projection;

import java.time.LocalDateTime;

public interface CommentSummaryProjection {
    Long getId();

    String getContent();

    AuthorSummary getAuthor();

    ReplyToSummary getReplyTo();

    LocalDateTime getCreatedTime();

    LocalDateTime getModifiedTime();

    Integer getVoteCount();

    interface AuthorSummary{
        String getUsername();

        String getProfilePicturePath();
    }

    interface ReplyToSummary{
        Long getId();
    }
}
