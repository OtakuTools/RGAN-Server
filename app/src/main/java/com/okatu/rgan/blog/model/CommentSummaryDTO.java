package com.okatu.rgan.blog.model;

import com.okatu.rgan.blog.model.projection.CommentSummaryProjection;

import java.time.LocalDateTime;

public class CommentSummaryDTO {
    private Long id;

    private String content;

    private String authorName;

    private Long replyTo;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    private Integer voteCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public CommentSummaryDTO() {
    }

    public CommentSummaryDTO(Long id, String content, String authorName, Long replyTo, LocalDateTime createdTime, LocalDateTime modifiedTime, Integer voteCount) {
        this.id = id;
        this.content = content;
        this.authorName = authorName;
        this.replyTo = replyTo;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.voteCount = voteCount;
    }

    public static CommentSummaryDTO convertFrom(CommentSummaryProjection projection){
        Long replyToCommentId = projection.getReplyTo() != null ?
            projection.getReplyTo().getId() : null;
        return new CommentSummaryDTO(
            projection.getId(),
            projection.getContent(),
            projection.getAuthor().getUsername(),
            replyToCommentId,
            projection.getCreatedTime(),
            projection.getModifiedTime(),
            projection.getVoteCount()
        );
    }
}
