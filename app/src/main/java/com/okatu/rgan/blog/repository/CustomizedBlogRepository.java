package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.entity.Blog;

import java.util.Collection;
import java.util.List;

public interface CustomizedBlogRepository {
    List<Blog> findByTitleContainsAnyOfKeywords(Collection<String> keywords);
}
