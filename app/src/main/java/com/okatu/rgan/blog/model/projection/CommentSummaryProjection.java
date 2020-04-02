package com.okatu.rgan.blog.model.projection;

import java.time.LocalDateTime;

public interface CommentSummaryProjection {
    Long getId();

    String getContent();

    AuthorSummary getAuthor();

    ReplyToSummary getReplyTo();

    LocalDateTime getCreatedTime();

    LocalDateTime getModifiedTime();

    interface AuthorSummary{
        String getUsername();
    }

    interface ReplyToSummary{
        Long getId();
    }
}
