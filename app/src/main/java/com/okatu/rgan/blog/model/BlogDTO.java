package com.okatu.rgan.blog.model;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Tag;
import com.okatu.rgan.user.model.AuthorInCreateEntityDTO;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public class BlogDTO {
    private Long id;

    private String title;

    private Integer type;

    private Integer status;

    private String summary;

    private String content;

    private Integer voteCount;

    private Integer visitorCount = 0;

    private Set<Tag> tags = new LinkedHashSet<>();

    private AuthorInCreateEntityDTO author;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public AuthorInCreateEntityDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInCreateEntityDTO author) {
        this.author = author;
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

    public static BlogDTO convertFrom(Blog blog){
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setType(blog.getType().getValue());
        blogDTO.setStatus(blog.getStatus().getValue());
        blogDTO.setSummary(blog.getSummary());
        blogDTO.setContent(blog.getContent());
        blogDTO.setCreatedTime(blog.getCreatedTime());
        blogDTO.setModifiedTime(blog.getModifiedTime());
        blogDTO.setTags(blog.getTags());
        blogDTO.setAuthor(new AuthorInCreateEntityDTO(blog.getAuthor().getUsername(), blog.getAuthor().getProfilePicturePath()));
        blogDTO.setVoteCount(blog.getVoteCounter().getValue());
        blogDTO.setVisitorCount(blog.getVisitorCount());
        return blogDTO;
    }
}
