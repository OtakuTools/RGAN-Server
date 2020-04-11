package com.okatu.rgan.vote.repository;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BlogVoteItemRepository extends JpaRepository<BlogVoteItem, Long> {
    Optional<BlogVoteItem> findByBlogAndAuthor(Blog blog, RganUser user);

    List<BlogVoteItem> findByBlog_IdInAndAuthor(Collection<Long> blogId, RganUser author);
}
