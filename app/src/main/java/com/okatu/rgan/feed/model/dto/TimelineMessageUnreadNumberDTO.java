package com.okatu.rgan.feed.model.dto;

public class TimelineMessageUnreadNumberDTO {
    private Integer upVoteNum;

    private Integer commentNum;

    public Integer getUpVoteNum() {
        return upVoteNum;
    }

    public void setUpVoteNum(Integer upVoteNum) {
        this.upVoteNum = upVoteNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }
}
