package com.okatu.rgan.vote.repository;

import com.okatu.rgan.vote.model.entity.CommentVoteCounter;

public interface CustomizedCommentVoteCounterRepository {
    void changeVoteCount(int value, CommentVoteCounter voteCounter);
}
