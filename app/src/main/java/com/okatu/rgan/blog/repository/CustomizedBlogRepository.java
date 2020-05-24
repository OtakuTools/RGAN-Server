package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.model.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface CustomizedBlogRepository {
    Page<BlogSummaryDTO> findByTitleContainsAnyOfKeywordsAndStatusPublished(Collection<String> keywords, Pageable pageable);
}
