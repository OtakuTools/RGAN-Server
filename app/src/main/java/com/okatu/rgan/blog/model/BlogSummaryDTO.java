package com.okatu.rgan.blog.model;

import com.okatu.rgan.blog.constant.BlogStatus;
import com.okatu.rgan.blog.constant.BlogType;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.projection.BlogSummaryProjection;
import com.okatu.rgan.user.model.AuthorInCreateEntityDTO;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BlogSummaryDTO{
    private Long id;

    private String title;

    private String summary;

    private Integer type;

    private Integer status;

    private Integer voteCount;

    private Integer visitorCount;

    private AuthorInCreateEntityDTO author;

    private Set<TagSummaryDTO> tags;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    public BlogSummaryDTO() {
    }

    // tags field cannot...
    public BlogSummaryDTO(
        Long id, String title,
        String summary,
        BlogType type, BlogStatus blogStatus,
        Integer voteCount, Integer visitorCount,
        String authorName, String authorProfilePicturePath,
        LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.type = type.getValue();
        this.status = blogStatus.getValue();
        this.voteCount = voteCount;
        this.visitorCount = visitorCount;
        this.author = new AuthorInCreateEntityDTO(authorName, authorProfilePicturePath);
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

    public Integer getVoteCount() {
        return voteCount;
    }

    public Integer getVisitorCount() {
        return visitorCount;
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

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public void setVisitorCount(Integer visitorCount) {
        this.visitorCount = visitorCount;
    }

    public AuthorInCreateEntityDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInCreateEntityDTO author) {
        this.author = author;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static BlogSummaryDTO convertFrom(BlogSummaryProjection blogSummaryProjection){
        BlogSummaryDTO blogSummaryDTO = new BlogSummaryDTO(
            blogSummaryProjection.getId(),
            blogSummaryProjection.getTitle(),
            blogSummaryProjection.getSummary(),
            blogSummaryProjection.getType(),
            blogSummaryProjection.getStatus(),
            blogSummaryProjection.getVoteCount(),
            blogSummaryProjection.getVisitorCount(),
            blogSummaryProjection.getAuthor().getUsername(),
            blogSummaryProjection.getAuthor().getProfilePicturePath(),
            blogSummaryProjection.getCreatedTime(),
            blogSummaryProjection.getModifiedTime()
        );
        blogSummaryDTO.setTags(blogSummaryProjection.getTags().stream().map(TagSummaryDTO::convertFrom).collect(Collectors.toCollection(LinkedHashSet::new)));
        return blogSummaryDTO;
    }

    public static BlogSummaryDTO convertFrom(Blog blog){
        BlogSummaryDTO blogSummaryDTO = new BlogSummaryDTO(
            blog.getId(),
            blog.getTitle(),
            blog.getSummary(),
            blog.getType(),
            blog.getStatus(),
            blog.getVoteCount(),
            blog.getVisitorCount(),
            blog.getAuthor().getUsername(),
            blog.getAuthor().getProfilePicturePath(),
            blog.getCreatedTime(),
            blog.getModifiedTime()
        );

        blogSummaryDTO.setTags(blog.getTags().stream().map(TagSummaryDTO::convertFrom).collect(Collectors.toSet()));
        return blogSummaryDTO;
    }
}
