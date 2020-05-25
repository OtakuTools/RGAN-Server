package com.okatu.rgan.feed.model;

import com.okatu.rgan.blog.model.entity.Comment;

import java.time.LocalDateTime;

public class TimelineCommentDTO {
    private Long id;

    private String content;

    private String authorName;

    private Long blogId;

    private String blogTitle;

    private Long replyTo;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

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

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
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

    public TimelineCommentDTO(Long id, String content, String authorName, Long blogId, String blogTitle, Long replyTo, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.content = content;
        this.authorName = authorName;
        this.blogId = blogId;
        this.blogTitle = blogTitle;
        this.replyTo = replyTo;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public static TimelineCommentDTO convertFrom(Comment comment){
        Long replyToCommentId = comment.getReplyTo() != null ?
            comment.getReplyTo().getId() : null;

        return new TimelineCommentDTO(
            comment.getId(),
            comment.getContent(),
            comment.getAuthor().getUsername(),
            comment.getBlog().getId(),
            comment.getBlog().getTitle(),
            replyToCommentId,
            comment.getCreatedTime(),
            comment.getModifiedTime()
        );
    }
}

