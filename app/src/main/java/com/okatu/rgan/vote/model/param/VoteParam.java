package com.okatu.rgan.vote.model.param;

import com.okatu.rgan.vote.constant.VoteStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class VoteParam {

    @NotNull
    private VoteStatus status;

    public VoteStatus getStatus() {
        return status;
    }

    public void setStatus(VoteStatus status) {
        this.status = status;
    }
}
