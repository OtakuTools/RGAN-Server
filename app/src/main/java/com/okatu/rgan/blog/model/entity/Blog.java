package com.okatu.rgan.blog.model.entity;

import com.okatu.rgan.blog.constant.BlogStatus;
import com.okatu.rgan.blog.constant.BlogType;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.model.VoteAbleEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

// `findBlogByTagId` is definitely needed
// but what about a `findTagIdByBlog`? never
// unidirectional relationship is enough
@Entity
public class Blog implements VoteAbleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(nullable = false)
    @Convert(converter = BlogType.Converter.class)
    private BlogType type;

    @Column(nullable = false)
    @Convert(converter = BlogStatus.Converter.class)
    private BlogStatus status;

    @Column(columnDefinition = "text")
    private String content;

    // might it cause cache miss?
    // since the voteCount field might be update frequently
    // evolution in different frequency comparing to other fields
    @Column(nullable = false)
    private Integer voteCount = 0;

    private Integer visitorCount = 0;

    // ugly here
    // i want the Set semantic and performance enhancement(in jpa),
    // but i also want to keep the tag insertion order
    // however, keep the insertion order itself is considered as anti-pattern in database
    // and this method(declared as LinkedHashSet cannot guarantee nothing at all
    @ManyToMany
    @JoinTable(
        name = "blog_tag_association",
        joinColumns = @JoinColumn(name = "blog_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    // use @ManyToMany only here, since the relationship here is quite stable and simple,
    // unlike follow or favourite, both of which need extra column for description,
    // and mapping such join table with extra column seems a bit of tricky in jpa.
    private Set<Tag> tags = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private RganUser author;

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "created_time", updatable = false, insertable = false,
//        columnDefinition = "datetime default CURRENT_TIMESTAMP")
//    @Generated(value = GenerationTime.ALWAYS)
    @Column(updatable = false)
    private LocalDateTime createdTime;

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "modified_time",
//        columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
//    @Generated(value = GenerationTime.ALWAYS)
    private LocalDateTime modifiedTime;

    private String summary;

    @PrePersist
    private void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdTime = now;
        modifiedTime = now;
    }

    // here's the problem
    // the voteCount field might be modified
    // thus trigger the preUpdate
    // so update this field by hand
//    @PreUpdate
//    private void preUpdate(){
//        modifiedTime = LocalDateTime.now();
//    }


    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public BlogStatus getStatus() {
        return status;
    }

    public void setStatus(BlogStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BlogType getType() { return type; }

    public void setType(BlogType type) { this.type = type; }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
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

    public RganUser getAuthor() {
        return author;
    }

    public void setAuthor(RganUser author) {
        this.author = author;
    }

    public Blog() {
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
