package com.okatu.rgan.vote.model;

public interface VoteAbleEntity {
    void incrVoteCount(int value);

    void decrVoteCount(int value);
}
