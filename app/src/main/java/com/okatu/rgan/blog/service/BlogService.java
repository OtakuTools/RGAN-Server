package com.okatu.rgan.blog.service;

import com.okatu.rgan.blog.constant.BlogStatus;
import com.okatu.rgan.blog.model.BlogDTO;
import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Tag;
import com.okatu.rgan.blog.model.param.BlogEditParam;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.blog.repository.TagRepository;
import com.okatu.rgan.common.exception.InvalidRequestParameterException;
import com.okatu.rgan.common.exception.ResourceAccessDeniedException;
import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.model.entity.BlogVoteCounter;
import com.okatu.rgan.vote.repository.BlogVoteCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BlogVoteCounterRepository blogVoteCounterRepository;

    public Page<BlogSummaryDTO> getAuthorsPublishedBlogsOrderByCreatedTimeDesc(Collection<RganUser> authors, Pageable pageable){
        return blogRepository.findByAuthorInAndStatusOrderByCreatedTimeDesc(authors, BlogStatus.PUBLISHED, pageable).map(BlogSummaryDTO::convertFrom);
    }

    public Page<BlogSummaryDTO> getAllPublishedBlogsOrderByCreatedTimeDesc(Pageable pageable){
        return blogRepository.findByStatusOrderByCreatedTimeDesc(BlogStatus.PUBLISHED, pageable).map(BlogSummaryDTO::convertFrom);
    }

    public BlogDTO getPublishedBlogById(long id){
        // we are not enabling query cache, so...
//        return blogRepository.findById(id).filter(blog -> blog.getStatus() == BlogStatus.PUBLISHED).map(BlogDTO::convertFrom)
//            .orElseThrow(() -> new ResourceNotFoundException("blog", id));
        return blogRepository.findByIdAndStatus(id, BlogStatus.PUBLISHED).map(BlogDTO::convertFrom)
            .orElseThrow(() -> new ResourceNotFoundException("blog", id));
    }

    public Page<BlogSummaryDTO> getAuthorSpecificStatusBlogsOrderByCreatedTimeDesc(@NonNull RganUser author, BlogStatus status, Pageable pageable){
        return blogRepository.findByAuthorAndStatusOrderByCreatedTimeDesc(author, status, pageable).map(BlogSummaryDTO::convertFrom);
    }

    public Page<BlogSummaryDTO> getAuthorAllBlogsOrderByCreatedTimeDesc(@NonNull RganUser author, Pageable pageable){
        return blogRepository.findByAuthorOrderByCreatedTime(author, pageable).map(BlogSummaryDTO::convertFrom);
    }

    @Transactional
    public BlogDTO addBlog(BlogEditParam blogEditParam, @NonNull RganUser user){
        LinkedHashSet<Tag> tags = findTagsByTitles(blogEditParam.getTags());

        Blog blog = new Blog();
        blog.setTitle(blogEditParam.getTitle());
        blog.setType(blogEditParam.getType());

        if(blogEditParam.getStatus().equals(BlogStatus.DELETED)){
            throw new InvalidRequestParameterException("Cannot add a deleted blog!");
        }
        blog.setStatus(blogEditParam.getStatus());

        blog.setSummary(blogEditParam.getSummary());
        blog.setContent(blogEditParam.getContent());
        blog.setTags(tags);
        blog.setAuthor(user);

        Blog saved = blogRepository.save(blog);
        BlogVoteCounter voteCounter = new BlogVoteCounter(saved);
        saved.setVoteCounter(blogVoteCounterRepository.save(voteCounter));
        return BlogDTO.convertFrom(saved);
    }

    public BlogDTO editBlog(long id, BlogEditParam blogEditParam, @NonNull RganUser user){
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("blog", id));

        if(RganUser.isNotSame(blog.getAuthor(), user)){
            throw new ResourceAccessDeniedException("you have no permission to edit this blog");
        }

        blog.setType(blogEditParam.getType());
        blog.setStatus(blogEditParam.getStatus());
        blog.setTitle(blogEditParam.getTitle());
        blog.setSummary(blogEditParam.getSummary());
        blog.setContent(blogEditParam.getContent());
        blog.setModifiedTime(LocalDateTime.now());
        blog.setTags(findTagsByTitles(blogEditParam.getTags()));

        return BlogDTO.convertFrom(blogRepository.save(blog));
    }

    public void deleteBlog(long id, @NonNull RganUser user){
        blogRepository.findById(id).ifPresent(blog -> {
            if(RganUser.isNotSame(blog.getAuthor(), user)){
                throw new ResourceAccessDeniedException("you have no permission to delete this blog");
            }

            blog.setStatus(BlogStatus.DELETED);
            blog.setModifiedTime(LocalDateTime.now());
            blogRepository.save(blog);
        });
    }

    public Page<BlogSummaryDTO> searchPublishedBlogsByKeywordOrderByCreatedTimeDesc(Collection<String> keywords, Pageable pageable){
        return blogRepository.findByTitleContainsAnyOfKeywordsAndStatusPublished(keywords, pageable);
    }

    public Page<BlogSummaryDTO> searchPublishedBlogsByUsernameOrderByCreatedTimeDesc(String username, Pageable pageable){
        return blogRepository.findByAuthor_UsernameAndStatusOrderByCreatedTimeDesc(username, BlogStatus.PUBLISHED, pageable).map(BlogSummaryDTO::convertFrom);
    }

    private LinkedHashSet<Tag> findTagsByTitles(LinkedHashSet<String> titles){
//      put if absent
        return titles.stream().map(
            title -> tagRepository.findByTitle(title).orElseGet(() -> tagRepository.save(new Tag(title, title)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
