package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.model.projection.CommentSummaryProjection;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(value = "comment.voteCounter", type = EntityGraph.EntityGraphType.LOAD)
    List<Comment> findByBlog(Blog blog);

    @EntityGraph(value = "comment.voteCounter", type = EntityGraph.EntityGraphType.LOAD)
    Page<Comment> findByAuthorOrderByCreatedTimeDesc(RganUser author, Pageable pageable);
}
