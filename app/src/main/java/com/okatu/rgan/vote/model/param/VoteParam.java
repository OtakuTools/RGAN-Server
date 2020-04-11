package com.okatu.rgan.vote.model.param;

import com.okatu.rgan.vote.constant.VoteStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class VoteParam {

    @NotNull
    @Max(value = VoteStatus.UPVOTE)
    @Min(value = VoteStatus.DOWNVOTE)
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
