package com.okatu.rgan.blog.model.projection;

import java.time.LocalDateTime;

public interface CommentSummaryProjection {
    Long getId();

    String getContent();

    AuthorSummary getAuthor();

    ReplyToSummary getReplyTo();

    LocalDateTime getCreatedTime();

    LocalDateTime getModifiedTime();

    VoteCounter getVoteCounter();

    interface AuthorSummary{
        String getUsername();

        String getProfilePicturePath();
    }

    interface ReplyToSummary{
        Long getId();
    }

    interface VoteCounter {
        Integer getValue();
    }
}
