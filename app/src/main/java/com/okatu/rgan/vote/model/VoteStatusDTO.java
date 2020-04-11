package com.okatu.rgan.vote.model;

import com.okatu.rgan.vote.constant.VoteStatus;
import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import com.okatu.rgan.vote.model.entity.CommentVoteItem;

public class VoteStatusDTO {
    private Long entityId;

    private Integer status;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public VoteStatusDTO(Long entityId, Integer status) {
        this.entityId = entityId;
        this.status = status;
    }

    public static VoteStatusDTO convertFrom(BlogVoteItem blogVoteItem){
        return new VoteStatusDTO(blogVoteItem.getBlog().getId(), blogVoteItem.getStatus());
    }

    public static VoteStatusDTO convertFrom(CommentVoteItem commentVoteItem){
        return new VoteStatusDTO(commentVoteItem.getComment().getId(), commentVoteItem.getStatus());
    }
}
