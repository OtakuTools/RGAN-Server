package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.constant.BlogStatus;
import com.okatu.rgan.blog.model.projection.BlogSummaryProjection;
import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

// What choice have you got?
// result mapping is for native query
// for the Entity, can use simple JPQL and designate fetch strategy
// but for projection?
// or, we need to cache the projection?
// and we need to use mechanism than can be integrated into current system
// like l2 cache, stuff like UserSummary need to be cached
public interface BlogRepository extends JpaRepository<Blog, Long>, CustomizedBlogRepository {

    Page<BlogSummaryProjection> findByAuthorInAndStatusOrderByCreatedTimeDesc(Collection<RganUser> author, BlogStatus status, Pageable pageable);

    Page<BlogSummaryProjection> findByStatusOrderByCreatedTimeDesc(BlogStatus status, Pageable pageable);

    Page<BlogSummaryProjection> findByAuthor_UsernameAndStatusOrderByCreatedTimeDesc(String username, BlogStatus status, Pageable pageable);

    Page<BlogSummaryProjection> findByAuthorOrderByCreatedTime(RganUser author, Pageable pageable);

    Page<BlogSummaryProjection> findByAuthorAndStatusOrderByCreatedTimeDesc(RganUser author, BlogStatus status, Pageable pageable);

//    @Query(value = "SELECT b FROM Blog b join fetch b.voteCounter WHERE b.id=?1 AND b.status=?2")
    Optional<Blog> findByIdAndStatus(Long id, BlogStatus status);
}
