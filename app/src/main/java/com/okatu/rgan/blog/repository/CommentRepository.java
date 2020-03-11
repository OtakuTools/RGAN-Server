package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.model.projection.CommentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<CommentProjection> findByBlog_Id(Long blogId, Pageable pageable);
}
