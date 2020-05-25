package com.okatu.rgan.vote.model;

import com.okatu.rgan.user.model.RganUser;

public interface VoteAbleEntity {
    void incrVoteCount(int value);

    void decrVoteCount(int value);

    RganUser getAuthor();
}
