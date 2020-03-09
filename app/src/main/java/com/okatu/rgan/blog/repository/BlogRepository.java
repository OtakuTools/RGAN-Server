package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, CustomizedBlogRepository {

    Page<Blog> findByUserInOrderByCreatedTimeDesc(Collection<RganUser> users, Pageable pageable);
}
