package com.okatu.rgan.blog.model.entity;

import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.model.entity.CommentVoteCounter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NamedEntityGraph(
    name = "comment.voteCounter",
    attributeNodes = {@NamedAttributeNode(value = "voteCounter")}
)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Transient
    private Integer status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private RganUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_id")
    private Comment replyTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blog_id", nullable = false, updatable = false)
    private Blog blog;

    @Column(updatable = false)
    private LocalDateTime createdTime;

    // see the comment in Blog
    // same reason to remove the @PreUpdate
    private LocalDateTime modifiedTime;

//    private Integer voteCount = 0;
    @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @PrimaryKeyJoinColumn
    private CommentVoteCounter voteCounter;

    public CommentVoteCounter getVoteCounter() {
        return voteCounter;
    }

    public void setVoteCounter(CommentVoteCounter voteCounter) {
        this.voteCounter = voteCounter;
    }

    @PrePersist
    private void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdTime = now;
        modifiedTime = now;
    }
//
    @PreUpdate
    private void preUpdate(){
        modifiedTime = LocalDateTime.now();
    }

//    public Integer getVoteCount() {
//        return voteCount;
//    }
//
//    public void setVoteCount(Integer voteCount) {
//        this.voteCount = voteCount;
//    }

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
}
