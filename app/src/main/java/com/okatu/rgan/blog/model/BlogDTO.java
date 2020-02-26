package com.okatu.rgan.blog.model;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Tag;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class BlogDTO {
    private Long id;

    private String title;

    private String content;

    private Integer upvoteCount = 0;

    private Integer visitorCount = 0;

    private Set<Tag> tags = new LinkedHashSet<>();

    private String username;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(Integer upvoteCount) {
        this.upvoteCount = upvoteCount;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
            ", content='" + content + '\'' +
            ", upvoteCount=" + upvoteCount +
            ", visitorCount=" + visitorCount +
            ", tags=" + tags +
            ", username='" + username + '\'' +
            ", createdTime=" + createdTime +
            ", modifiedTime=" + modifiedTime +
            '}';
    }

    public static BlogDTO convertFrom(Blog blog){
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setContent(blog.getContent());
        blogDTO.setCreatedTime(blog.getCreatedTime());
        blogDTO.setModifiedTime(blog.getModifiedTime());
        blogDTO.setTags(blog.getTags());
        blogDTO.setUsername(blog.getUser().getUsername());
        blogDTO.setUpvoteCount(blog.getUpvoteCount());
        blogDTO.setVisitorCount(blog.getVisitorCount());
        return blogDTO;
    }
}
