package com.okatu.rgan.blog.model.entity;

import com.okatu.rgan.user.model.RganUser;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

// `findBlogByTagId` is definitely needed
// but what about a `findTagIdByBlog`? never
// unidirectional relationship is enough
@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    private Integer upvoteCount = 0;

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
    private Set<Tag> tags = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "author_id")
    private RganUser user;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", updatable = false, insertable = false,
        columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @Generated(value = GenerationTime.ALWAYS)
    private LocalDateTime createdTime;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time",
        columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Generated(value = GenerationTime.ALWAYS)
    private LocalDateTime modifiedTime;

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

    public RganUser getUser() {
        return user;
    }

    public void setUser(RganUser user) {
        this.user = user;
    }

    public Blog() {
    }

}
