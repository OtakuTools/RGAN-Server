package com.okatu.rgan.feed.model.dto;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;
import com.okatu.rgan.vote.constant.VoteType;
import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import com.okatu.rgan.vote.model.entity.CommentVoteItem;

public class TimelineUpVoteDTO extends TimelineDetailResultBasic {

    // blog, comment
    // what else?
    private Integer voteType;

    // always provide
    private Long blogId;

    // always provide
    private String blogTitle;

    // for blog vote, null
    private Long targetCommentId;

    // for blog, null
    // for comment, comment content
    private String targetCommentContent;

    public Integer getVoteType() {
        return voteType;
    }

    public void setVoteType(Integer voteType) {
        this.voteType = voteType;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getTargetCommentId() {
        return targetCommentId;
    }

    public void setTargetCommentId(Long targetCommentId) {
        this.targetCommentId = targetCommentId;
    }

    public String getTargetCommentContent() {
        return targetCommentContent;
    }

    public void setTargetCommentContent(String targetCommentContent) {
        this.targetCommentContent = targetCommentContent;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public TimelineUpVoteDTO(FeedMessageBoxItem feedMessageBoxItem) {
        super(feedMessageBoxItem);
    }

    public static TimelineUpVoteDTO createFrom(FeedMessageBoxItem feedMessageBoxItem, BlogVoteItem blogVoteItem){
        TimelineUpVoteDTO timelineUpVoteDTO = new TimelineUpVoteDTO(feedMessageBoxItem);

        timelineUpVoteDTO.setBlogId(blogVoteItem.getBlog().getId());
        timelineUpVoteDTO.setBlogTitle(blogVoteItem.getBlog().getTitle());
        timelineUpVoteDTO.setVoteType(VoteType.BLOG);
        timelineUpVoteDTO.setTargetCommentId(null);
        timelineUpVoteDTO.setTargetCommentContent(null);

        return timelineUpVoteDTO;
    }

    public static TimelineUpVoteDTO createFrom(FeedMessageBoxItem feedMessageBoxItem, CommentVoteItem commentVoteItem){
        TimelineUpVoteDTO timelineUpVoteDTO = new TimelineUpVoteDTO(feedMessageBoxItem);

        Comment comment = commentVoteItem.getComment();
        timelineUpVoteDTO.setBlogId(comment.getBlog().getId());
        timelineUpVoteDTO.setBlogTitle(comment.getBlog().getTitle());
        timelineUpVoteDTO.setVoteType(VoteType.COMMENT);
        timelineUpVoteDTO.setTargetCommentId(comment.getId());
        timelineUpVoteDTO.setTargetCommentContent(comment.getContent());

        return timelineUpVoteDTO;
    }
}
