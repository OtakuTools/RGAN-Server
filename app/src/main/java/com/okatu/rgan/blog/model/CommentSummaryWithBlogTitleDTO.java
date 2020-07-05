package com.okatu.rgan.blog.model;

import com.okatu.rgan.blog.model.entity.Comment;

import java.time.LocalDateTime;

public class CommentSummaryWithBlogTitleDTO extends CommentSummaryDTO {
    private Long blogId;

    private String blogTitle;

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

    public CommentSummaryWithBlogTitleDTO() {
    }

    public CommentSummaryWithBlogTitleDTO(Long id, String content, String authorName, String authorProfilePicturePath, Long replyTo, LocalDateTime createdTime, LocalDateTime modifiedTime, Integer voteCount) {
        super(id, content, authorName, authorProfilePicturePath, replyTo, createdTime, modifiedTime, voteCount);
    }

    public static CommentSummaryWithBlogTitleDTO convertFrom(Comment comment){
        Long replyToCommentId = comment.getReplyTo() != null ?
            comment.getReplyTo().getId() : null;
        CommentSummaryWithBlogTitleDTO commentSummaryWithBlogTitleDTO = new CommentSummaryWithBlogTitleDTO(
            comment.getId(),
            comment.getContent(),
            comment.getAuthor().getUsername(),
            comment.getAuthor().getProfilePicturePath(),
            replyToCommentId,
            comment.getCreatedTime(),
            comment.getModifiedTime(),
            comment.getVoteCounter().getValue()
        );

        commentSummaryWithBlogTitleDTO.setBlogId(comment.getBlog().getId());
        commentSummaryWithBlogTitleDTO.setBlogTitle(comment.getBlog().getTitle());
        return commentSummaryWithBlogTitleDTO;
    }
}
