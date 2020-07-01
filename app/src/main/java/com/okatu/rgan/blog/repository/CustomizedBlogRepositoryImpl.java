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
    public Page<BlogSummaryDTO> findByTitleContainsAnyOfKeywordsAndStatusPublished(Collection<String> keywords, Pageable pageable) {
        Assert.notEmpty(keywords, "keywords should not be empty");
        // tags field needs another query
        // very vulnerable
        // see https://stackoverflow.com/a/6004072/8510613
        // and https://stackoverflow.com/a/51496418/8510613
        // we just can't use collection in constructor
        StringBuilder based = new StringBuilder(
            "SELECT new com.okatu.rgan.blog.model.BlogSummaryDTO(" +
                "b.id, b.title, " +
                "b.summary, b.type, b.status, " +
                "b.voteCounter.value, b.visitorCount, " +
                "b.author.username, b.author.profilePicturePath, " +
                "b.createdTime, b.modifiedTime) FROM Blog b WHERE b.status=com.okatu.rgan.blog.constant.BlogStatus.PUBLISHED AND ");
        // LOWER(firstname) like '%' + LOWER(?0) + '%'
        based.append("(b.title like ?1");
        for(int i = 1, size = keywords.size(); i < size; i++){
            based.append(" OR b.title like ?").append(i + 1);
        }
        based.append(')');

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
//            Set<TagSummaryDTO> tags = entityManager.createQuery(
//                "SELECT t FROM Tag t JOIN Blog b WHERE b.id=?1", Tag.class)
//                .setParameter(1, blogSummaryDTO.getId())
//                .getResultList().stream().map(TagSummaryDTO::convertFrom).collect(Collectors.toCollection(LinkedHashSet::new));
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
