package com.okatu.rgan.vote.repository.impl;

import com.okatu.rgan.vote.model.entity.CommentVoteCounter;
import com.okatu.rgan.vote.repository.CustomizedCommentVoteCounterRepository;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CustomizedCommentVoteCounterRepositoryImpl implements CustomizedCommentVoteCounterRepository {
    private final SessionFactory sessionFactory;

    private final EntityManager entityManager;

    public CustomizedCommentVoteCounterRepositoryImpl(EntityManagerFactory entityManagerFactory, EntityManager entityManager){
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        this.entityManager = entityManager;
    }

    @Override
    public void changeVoteCount(int value, CommentVoteCounter voteCounter) {
        entityManager.createQuery("UPDATE CommentVoteCounter v SET v.value = v.value + ?1 WHERE v=?2")
            .setParameter(1, value)
            .setParameter(2, voteCounter)
            .executeUpdate();

        sessionFactory.getCache().evict(CommentVoteCounter.class, voteCounter.getId());
    }
}
