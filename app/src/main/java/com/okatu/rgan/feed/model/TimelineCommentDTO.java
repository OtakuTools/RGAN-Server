package com.okatu.rgan.feed.model;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;

import java.time.LocalDateTime;

public class TimelineCommentDTO extends TimelineResultDTO{

    private Long commentId;

    private String content;

    private Long blogId;

    private String blogTitle;

    private Long replyTo;

    private LocalDateTime modifiedTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public TimelineCommentDTO() {
    }

    public TimelineCommentDTO(FeedMessageBoxItem feedMessageBoxItem) {
        super(feedMessageBoxItem);
    }

    public static TimelineCommentDTO createFrom(FeedMessageBoxItem feedMessageBoxItem, Comment comment){
        TimelineCommentDTO timelineCommentDTO = new TimelineCommentDTO(feedMessageBoxItem);

        Long replyToCommentId = comment.getReplyTo() != null ?
            comment.getReplyTo().getId() : null;

        timelineCommentDTO.setCommentId(comment.getId());
        timelineCommentDTO.setContent(comment.getContent());
        timelineCommentDTO.setBlogId(comment.getBlog().getId());
        timelineCommentDTO.setBlogTitle(comment.getBlog().getTitle());
        timelineCommentDTO.setReplyTo(replyToCommentId);
        timelineCommentDTO.setModifiedTime(comment.getModifiedTime());

        return timelineCommentDTO;
    }
}

