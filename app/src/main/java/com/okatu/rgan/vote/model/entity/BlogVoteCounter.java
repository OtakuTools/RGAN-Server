package com.okatu.rgan.vote.model.entity;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.common.model.ManuallyAssignIdEntitySuperClass;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

// FUCK THIS
// see https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/
// for why not use unidirectional here
// FUCK
// REALLY RELIABLE HERE?
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class BlogVoteCounter extends ManuallyAssignIdEntitySuperClass<Long> {
    @Id
    private Long id;

////    @Id
//    @OneToOne(optional = false)
////    @PrimaryKeyJoinColumn
//    @JoinColumn(name = "id", updatable = false)
//    @MapsId
//    private Blog blog;

    @Column(nullable = false)
    private Integer value = 0;

    public BlogVoteCounter() {
    }

    public BlogVoteCounter(Blog blog) {
//        this.blog = blog;
        this.id = blog.getId();
    }
//
//    public Blog getBlog() {
//        return blog;
//    }


//    public BlogVoteCounter(Long id) {
//        this.id = id;
//    }

    public Long getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }

    public void changeVoteCount(int value){
        this.value += value;
    }
}
