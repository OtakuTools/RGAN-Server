package com.okatu.rgan.blog.repository;

import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.model.TagSummaryDTO;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Tag;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

public class CustomizedBlogRepositoryImpl implements CustomizedBlogRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<BlogSummaryDTO> findByTitleContainsAnyOfKeywords(Collection<String> keywords, Pageable pageable) {
        Assert.notEmpty(keywords, "keywords should not be empty");
        // tags field needs another query
        StringBuilder based = new StringBuilder(
            "SELECT new com.okatu.rgan.blog.model.BlogSummaryDTO(" +
                "b.id, b.title, " +
                "b.summary, " +
                "b.upvoteCount, b.visitorCount, " +
                "b.author.username, " +
                "b.createdTime, b.modifiedTime) FROM Blog b WHERE");
        // LOWER(firstname) like '%' + LOWER(?0) + '%'
        based.append(" b.title like ?1");
        for(int i = 1, size = keywords.size(); i < size; i++){
            based.append(" or b.title like ?").append(i + 1);
        }

        TypedQuery<BlogSummaryDTO> query = entityManager.createQuery(based.toString(), BlogSummaryDTO.class);

        int index = 1;
        for(String keyword : keywords){
            query.setParameter(index++, "%" + keyword + "%");
        }

        // pageNumber start from 0
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<BlogSummaryDTO> res = query.getResultList();

        res.forEach(blogSummaryDTO -> {
            @SuppressWarnings("unchecked")
            List<TagSummaryDTO> tags = entityManager.createNativeQuery(
                "SELECT tag.id as id, tag.title as title FROM blog_tag_association association " +
                    "INNER JOIN tag ON association.tag_id=tag.id " +
                    "WHERE association.blog_id=?1")
                .setParameter(1, blogSummaryDTO.getId())
                .getResultList();

//            LinkedHashSet<TagSummaryDTO> tags = tuples.stream()
//                .map(tuple -> new TagSummaryDTO(tuple.get("id", Long.class), tuple.get("title", String.class)))
//                .collect(Collectors.toCollection(LinkedHashSet::new));
            blogSummaryDTO.setTags(new LinkedHashSet<>(tags));
        });

        return new PageImpl<>(res, pageable, res.size());
    }
}
