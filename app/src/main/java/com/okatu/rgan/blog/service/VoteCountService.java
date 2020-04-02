package com.okatu.rgan.blog.service;

public interface VoteCountService {
    void incrBlogVoteCount(long blogId);

    void decrBlogVoteCount(long blogId);

    void incrCommentVoteCount(long commentId);

    void decrCommentVoteCount(long commentId);

    int getBlogVoteCount(long blogId);

    int getCommentVoteCount(long commentId);
}
