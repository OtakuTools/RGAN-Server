package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.projection.BlogSummaryProjection;
import com.okatu.rgan.blog.model.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BlogRepository extends JpaRepository<Blog, Long>, CustomizedBlogRepository {

    Page<BlogSummaryProjection> findByUser_IdInOrderByCreatedTimeDesc(Collection<Long> usersId, Pageable pageable);

    Page<Blog> findAllByOrderByCreatedTimeDesc(Pageable pageable);

    Page<BlogSummaryProjection> findByOrderByCreatedTimeDesc(Pageable pageable);
}
