package com.okatu.rgan.blog.model.entity;

import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.model.VoteAbleEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment implements VoteAbleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Transient
    private Integer status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private RganUser author;

    @ManyToOne
    @JoinColumn(name = "reply_to_id", nullable = true)
    private Comment replyTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    private Integer voteCount = 0;

    @PrePersist
    private void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdTime = now;
        modifiedTime = now;
    }

    @PreUpdate
    private void preUpdate(){
        modifiedTime = LocalDateTime.now();
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public RganUser getAuthor() {
        return author;
    }

    public void setAuthor(RganUser author) {
        this.author = author;
    }

    public Comment getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Comment replyTo) {
        this.replyTo = replyTo;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    @Override
    public void incrVoteCount(int value) {
        this.voteCount += value;
    }

    @Override
    public void decrVoteCount(int value) {
        this.voteCount -= value;
    }
}
