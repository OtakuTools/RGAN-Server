package com.okatu.rgan.vote.repository;

import com.okatu.rgan.vote.model.entity.BlogVoteCounter;


public interface CustomizedBlogVoteCounterRepository {
    void changeVoteCount(int value, BlogVoteCounter voteCounter);
}
