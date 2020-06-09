package com.okatu.rgan.vote.model.entity;

import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.constant.VoteStatus;
import com.okatu.rgan.vote.model.VoteAbleEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class VoteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private RganUser author;

    @Column(nullable = false)
    @Convert(converter = VoteStatus.Converter.class)
    private VoteStatus status;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    @PrePersist
    private void prePersist(){
        createdTime = LocalDateTime.now();
        modifiedTime = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate(){
        modifiedTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RganUser getAuthor() {
        return author;
    }

    public void setAuthor(RganUser author) {
        this.author = author;
    }

    public VoteStatus getStatus() {
        return status;
    }

    public void setStatus(VoteStatus status) {
        this.status = status;
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

    public abstract VoteAbleEntity getAssociateVoteAbleEntity();

    VoteItem(){

    }

    VoteItem(RganUser author) {
        this.author = author;
    }

}
