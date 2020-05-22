package com.okatu.rgan.blog.model;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Tag;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public class BlogDTO {
    private Long id;

    private String title;

    private Integer type;

    private String summary;

    private String content;

    private Integer voteCount;

    private Integer visitorCount = 0;

    private Set<Tag> tags = new LinkedHashSet<>();

    private String authorName;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() { return type; }

    public void setType(Integer type) { this.type = type; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUpvoteCount() {
        return voteCount;
    }

    public void setUpvoteCount(Integer upvoteCount) {
        this.voteCount = upvoteCount;
    }

    public Integer getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(Integer visitorCount) {
        this.visitorCount = visitorCount;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public String toString() {
        return "BlogDTO{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", type='" + type + '\'' +
            ", content='" + content + '\'' +
            ", voteCount=" + voteCount +
            ", visitorCount=" + visitorCount +
            ", tags=" + tags +
            ", username='" + authorName + '\'' +
            ", createdTime=" + createdTime +
            ", modifiedTime=" + modifiedTime +
            '}';
    }

    public static BlogDTO convertFrom(Blog blog){
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setType(blog.getType());
        blogDTO.setSummary(blog.getSummary());
        blogDTO.setContent(blog.getContent());
        blogDTO.setCreatedTime(blog.getCreatedTime());
        blogDTO.setModifiedTime(blog.getModifiedTime());
        blogDTO.setTags(blog.getTags());
        blogDTO.setAuthorName(blog.getAuthor().getUsername());
        blogDTO.setUpvoteCount(blog.getVoteCount());
        blogDTO.setVisitorCount(blog.getVisitorCount());
        return blogDTO;
    }
}
