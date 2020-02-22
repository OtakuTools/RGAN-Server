package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findBlogsByTitleIgnoreCaseContaining(String title);

}
