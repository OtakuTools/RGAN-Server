package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.entity.Blog;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CustomizedBlogRepositoryImpl implements CustomizedBlogRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Blog> findByTitleContainsAnyOfKeywords(Collection<String> keywords) {
        Assert.notEmpty(keywords, "keywords should not be empty");
        StringBuilder based = new StringBuilder("SELECT b FROM Blog b WHERE");
        based.append(" b.title like ?1");
        for(int i = 1, size = keywords.size(); i < size; i++){
            based.append(" or b.title like ?").append(i + 1);
        }

        Query query = entityManager.createQuery(based.toString());

        int index = 1;
        for(String keyword : keywords){
            query.setParameter(index++, "%" + keyword + "%");
        }

//        query.setFirstResult((page-1) * pageSize);
//        query.setMaxResults(pageSize);

        @SuppressWarnings("unchecked")
        List<Blog> blogs = (List<Blog>) query.getResultList();

        return blogs;
    }
}
