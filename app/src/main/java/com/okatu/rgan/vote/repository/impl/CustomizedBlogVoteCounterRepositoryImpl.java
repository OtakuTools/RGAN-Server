package com.okatu.rgan.vote.repository.impl;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.vote.model.entity.BlogVoteCounter;
import com.okatu.rgan.vote.repository.CustomizedBlogVoteCounterRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

public class CustomizedBlogVoteCounterRepositoryImpl implements CustomizedBlogVoteCounterRepository {
    private final SessionFactory sessionFactory;

    private final EntityManager entityManager;

    public CustomizedBlogVoteCounterRepositoryImpl(EntityManagerFactory entityManagerFactory, EntityManager entityManager){
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        this.entityManager = entityManager;
    }

    @Override
    public void changeVoteCount(int value, BlogVoteCounter voteCounter) {
        //noinspection JpaQlInspection
        entityManager.createQuery("UPDATE BlogVoteCounter v SET v.value = v.value + ?1 WHERE v=?2")
            .setParameter(1, value)
            .setParameter(2, voteCounter)
            .executeUpdate();

        sessionFactory.getCache().evict(BlogVoteCounter.class, voteCounter.getId());
    }
}
