package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.model.projection.CommentSummaryProjection;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentSummaryProjection> findByBlog_Id(Long blogId);
    Page<CommentSummaryProjection> findByAuthorOrderByCreatedTimeDesc(RganUser author, Pageable pageable);

    CommentSummaryProjection findByIdIs(Long id);
}
