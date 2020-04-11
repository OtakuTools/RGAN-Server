package com.okatu.rgan.vote.repository;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.model.entity.CommentVoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommentVoteItemRepository extends JpaRepository<CommentVoteItem, Long> {
    Optional<CommentVoteItem> findByCommentAndAuthor(Comment comment, RganUser author);

    List<CommentVoteItem> findByComment_IdInAndAuthor(Collection<Long> comment_id, RganUser author);
}
