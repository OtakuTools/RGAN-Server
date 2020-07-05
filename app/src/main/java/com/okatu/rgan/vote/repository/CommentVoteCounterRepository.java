package com.okatu.rgan.vote.repository;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.vote.model.entity.CommentVoteCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentVoteCounterRepository extends JpaRepository<CommentVoteCounter, Comment> {
    @Modifying
    @Query(value = "UPDATE CommentVoteCounter v SET v.value = v.value + ?1 WHERE v=?2")
    void changeVoteCount(int value, CommentVoteCounter voteCounter);
}
