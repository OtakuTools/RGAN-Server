package com.okatu.rgan.blog.controller;

import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.common.exception.ConstraintViolationException;
import com.okatu.rgan.common.exception.ResourceAccessDeniedException;
import com.okatu.rgan.blog.model.BlogDTO;
import com.okatu.rgan.blog.model.param.BlogEditParam;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Tag;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.blog.repository.TagRepository;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping
    public Page<BlogSummaryDTO> all(@PageableDefault Pageable pageable) {
        return blogRepository.findByOrderByCreatedTimeDesc(pageable).map(BlogSummaryDTO::convertFrom);
    }

    @PostMapping
    public BlogDTO add(@RequestBody BlogEditParam blogEditParam, @AuthenticationPrincipal RganUser user){
        LinkedHashSet<Tag> tags = findTagsByTitles(blogEditParam.getTags());
        Blog blog = new Blog();
        blog.setTitle(blogEditParam.getTitle());
        blog.setType(blogEditParam.getType());
        blog.setSummary(blogEditParam.getSummary());
        blog.setContent(blogEditParam.getContent());
        blog.setTags(tags);
        blog.setAuthor(userRepository.findByUsername(user.getUsername())
            .orElseThrow(() -> new ConstraintViolationException("Cannot find current request username in repository, username: " + user.getUsername()))
        );
        blog.setVoteCount(0);

        Blog saved = blogRepository.save(blog);
//        applicationEventPublisher.publishEvent(new BlogPublishEvent(this, blog));
        return BlogDTO.convertFrom(saved);
    }

    // visitor count problem:
    // front-end event track, involve some stream processing framework to handle.
    // or just intercept back-end
    // however, if configure each get method
    // stupid
    // if use some filter/advice/intercept mechanism
    // involve a question, visitor count is highly associated with entity
    // how to get the entity concept in filter class?
    // or we involve some centralized visitor count mechanism?

    // Multiple servlets executing request threads may have active access to the same session object at the same time.
    // The container must ensure that manipulation of internal data structures representing the session attributes
    // is performed in a threadsafe manner.
    // The Developer has the responsibility for threadsafe access to the attribute objects themselves.
    // This will protect the attribute collection inside the HttpSession object from concurrent access,
    // eliminating the opportunity for an application to cause that collection to become corrupted.
    @GetMapping("/{id}")
    public BlogDTO one(@PathVariable Long id, HttpSession session){
        return blogRepository.findById(id).map(BlogDTO::convertFrom)
            .orElseThrow(() -> new ResourceNotFoundException("blog", id));
    }

    @PutMapping("/{id}")
    public BlogDTO edit(@PathVariable Long id, @RequestBody BlogEditParam blogEditParam, @AuthenticationPrincipal RganUser user){

        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("blog", id));

        if(RganUser.isNotSame(blog.getAuthor(), user)){
            throw new ResourceAccessDeniedException("you have no permission to edit this blog");
        }

        blog.setType(blogEditParam.getType());
        blog.setTitle(blogEditParam.getTitle());
        blog.setSummary(blogEditParam.getSummary());
        blog.setContent(blogEditParam.getContent());
        blog.setTags(findTagsByTitles(blogEditParam.getTags()));

        return BlogDTO.convertFrom(blogRepository.save(blog));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal RganUser user){
        blogRepository.findById(id).ifPresent(blog -> {
            if(RganUser.isNotSame(blog.getAuthor(), user)){
                throw new ResourceAccessDeniedException("you have no permission to delete this blog");
            }
            blogRepository.deleteById(id);
        });
    }

    @GetMapping("/search")
    public Page<BlogSummaryDTO> search(@RequestParam(value = "keyword", required = false) String keyword, @PageableDefault Pageable pageable){
        if(StringUtils.isEmpty(keyword)){
            return blogRepository.findByOrderByCreatedTimeDesc(pageable).map(BlogSummaryDTO::convertFrom);
        }

        String[] keywords = keyword.split(" ");

        return blogRepository.findByTitleContainsAnyOfKeywords(Arrays.asList(keywords), pageable);
    }

    @GetMapping("/user")
    public Page<BlogSummaryDTO> users(@RequestParam(value = "name") String name, @PageableDefault Pageable pageable) {
        return blogRepository.findByAuthor_UsernameOrderByCreatedTimeDesc(name, pageable).map(BlogSummaryDTO::convertFrom);
    }

    private LinkedHashSet<Tag> findTagsByTitles(LinkedHashSet<String> titles){
//        return titles.stream().map(
//            title -> tagRepository.findByTitle(title).orElseThrow(() -> new ResourceNotFoundException("Could not find Tag, title: " + title))
//        ).collect(Collectors.toCollection(LinkedHashSet::new));
        return titles.stream().map(
            title -> tagRepository.findByTitle(title).orElseGet(() -> tagRepository.save(new Tag(title, title)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
