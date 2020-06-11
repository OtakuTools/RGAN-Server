package com.okatu.rgan.blog.model;

import com.okatu.rgan.blog.model.projection.CommentSummaryProjection;
import com.okatu.rgan.user.model.AuthorInCreateEntityDTO;

import java.time.LocalDateTime;

public class CommentSummaryDTO {
    private Long id;

    private String content;

    private AuthorInCreateEntityDTO author;

    private Long replyTo;

    private Long blogId;

    private String blogTitle;

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

    public AuthorInCreateEntityDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInCreateEntityDTO author) {
        this.author = author;
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

    public CommentSummaryDTO(Long id, String content, String authorName, String authorProfilePicturePath, Long replyTo, LocalDateTime createdTime, LocalDateTime modifiedTime, Integer voteCount) {
        this.id = id;
        this.content = content;
        this.author = new AuthorInCreateEntityDTO(authorName, authorProfilePicturePath);
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
            projection.getAuthor().getProfilePicturePath(),
            replyToCommentId,
            projection.getCreatedTime(),
            projection.getModifiedTime(),
            projection.getVoteCount()
        );
    }
}
