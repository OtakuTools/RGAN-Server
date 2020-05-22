package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.projection.BlogSummaryProjection;
import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BlogRepository extends JpaRepository<Blog, Long>, CustomizedBlogRepository {

    Page<BlogSummaryProjection> findByAuthorInOrderByCreatedTimeDesc(Collection<RganUser> usersId, Pageable pageable);

    Page<BlogSummaryProjection> findByOrderByCreatedTimeDesc(Pageable pageable);

    Page<BlogSummaryProjection> findByAuthor_UsernameOrderByCreatedTimeDesc(String username, Pageable pageable);
}
