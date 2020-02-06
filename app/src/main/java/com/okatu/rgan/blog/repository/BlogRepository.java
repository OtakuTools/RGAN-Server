package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
