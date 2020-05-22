package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTitle(String title);

    Page<Tag> findByTitleContains(String keyword, Pageable pageable);

    boolean existsByTitle(String title);

}

