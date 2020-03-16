package com.okatu.rgan.blog.model;

import com.okatu.rgan.blog.model.entity.Tag;
import com.okatu.rgan.blog.model.projection.BlogSummaryProjection;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BlogSummaryDTO{
    private Long id;

    private String title;

    private String summary;

    private Integer upvoteCount;

    private Integer visitorCount;

    private String username;

    private Set<TagSummaryDTO> tags;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    public BlogSummaryDTO() {
    }

    // tags field cannot...
    public BlogSummaryDTO(
        Long id, String title,
        String summary,
        Integer upvoteCount, Integer visitorCount,
        String username,
        LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.upvoteCount = upvoteCount;
        this.visitorCount = visitorCount;
        this.username = username;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Integer getUpvoteCount() {
        return upvoteCount;
    }

    public Integer getVisitorCount() {
        return visitorCount;
    }

    public String getUsername() {
        return username;
    }

    public Set<TagSummaryDTO> getTags() {
        return tags;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setUpvoteCount(Integer upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public void setVisitorCount(Integer visitorCount) {
        this.visitorCount = visitorCount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTags(Set<TagSummaryDTO> tags) {
        this.tags = tags;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public static BlogSummaryDTO convertFrom(BlogSummaryProjection blogSummaryProjection){
        BlogSummaryDTO blogSummaryDTO = new BlogSummaryDTO(
            blogSummaryProjection.getId(),
            blogSummaryProjection.getTitle(),
            blogSummaryProjection.getSummary(),
            blogSummaryProjection.getUpvoteCount(),
            blogSummaryProjection.getVisitorCount(),
            blogSummaryProjection.getUser().getUsername(),
            blogSummaryProjection.getCreatedTime(),
            blogSummaryProjection.getModifiedTime()
        );
        blogSummaryDTO.setTags(blogSummaryProjection.getTags().stream().map(TagSummaryDTO::convertFrom).collect(Collectors.toCollection(LinkedHashSet::new)));
        return blogSummaryDTO;
    }
}
