package com.okatu.rgan.feed.model;

import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;
import com.okatu.rgan.vote.constant.VoteType;
import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import com.okatu.rgan.vote.model.entity.CommentVoteItem;

import java.time.LocalDateTime;

public class TimelineUpVoteDTO extends TimelineResultDTO{

    // blog, comment
    // what else?
    private Integer voteType;

    // always provide
    private Long blogId;

    // for blog vote, null
    private Long targetCommentId;

    // for blog, title
    // for comment, comment content
    private String voteTargetSummary;

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

    public String getVoteTargetSummary() {
        return voteTargetSummary;
    }

    public void setVoteTargetSummary(String voteTargetSummary) {
        this.voteTargetSummary = voteTargetSummary;
    }

    public TimelineUpVoteDTO(FeedMessageBoxItem feedMessageBoxItem) {
        super(feedMessageBoxItem);
    }

    public static TimelineUpVoteDTO createFrom(FeedMessageBoxItem feedMessageBoxItem, BlogVoteItem blogVoteItem){
        TimelineUpVoteDTO timelineUpVoteDTO = new TimelineUpVoteDTO(feedMessageBoxItem);

        timelineUpVoteDTO.setBlogId(blogVoteItem.getId());
        timelineUpVoteDTO.setVoteType(VoteType.BLOG);
        timelineUpVoteDTO.setTargetCommentId(null);
        timelineUpVoteDTO.setVoteTargetSummary(blogVoteItem.getBlog().getTitle());

        return timelineUpVoteDTO;
    }

    public static TimelineUpVoteDTO createFrom(FeedMessageBoxItem feedMessageBoxItem, CommentVoteItem commentVoteItem){
        TimelineUpVoteDTO timelineUpVoteDTO = new TimelineUpVoteDTO(feedMessageBoxItem);

        timelineUpVoteDTO.setBlogId(commentVoteItem.getId());
        timelineUpVoteDTO.setVoteType(VoteType.COMMENT);
        timelineUpVoteDTO.setTargetCommentId(commentVoteItem.getComment().getId());
        timelineUpVoteDTO.setVoteTargetSummary(commentVoteItem.getComment().getContent());

        return timelineUpVoteDTO;
    }
}
