package com.okatu.rgan.vote.repository;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.vote.model.entity.BlogVoteCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface BlogVoteCounterRepository extends JpaRepository<BlogVoteCounter, Blog>, CustomizedBlogVoteCounterRepository{
    // flush the change before this query
//    @Modifying(flushAutomatically = true)
//    @Query(value = "UPDATE BlogVoteCounter v SET v.value = v.value + ?1 WHERE v=?2")
//    void changeVoteCount(int value, BlogVoteCounter voteCounter);
}
