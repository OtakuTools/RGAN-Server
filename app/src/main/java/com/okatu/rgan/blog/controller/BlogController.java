package com.okatu.rgan.blog.controller;

import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.service.BlogService;
import com.okatu.rgan.common.exception.ConstraintViolationException;
import com.okatu.rgan.common.exception.ResourceAccessDeniedException;
import com.okatu.rgan.blog.model.BlogDTO;
import com.okatu.rgan.blog.model.param.BlogEditParam;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Tag;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.blog.repository.TagRepository;
import com.okatu.rgan.user.feature.service.UserFavouriteListService;
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
    private BlogService blogService;

    @Autowired
    private UserFavouriteListService userFavouriteListService;

    @GetMapping
    public Page<BlogSummaryDTO> all(@PageableDefault Pageable pageable) {
        return blogService.getAllPublishedBlogsOrderByCreatedTimeDesc(pageable);
    }

    @PostMapping
    public BlogDTO add(@RequestBody BlogEditParam blogEditParam, @AuthenticationPrincipal RganUser user){
        return blogService.addBlog(blogEditParam, user);
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
    public BlogDTO one(@PathVariable Long id){
        return blogService.getPublishedBlogById(id);
    }

    @PutMapping("/{id}")
    public BlogDTO edit(@PathVariable Long id, @RequestBody BlogEditParam blogEditParam, @AuthenticationPrincipal RganUser user){
        return blogService.editBlog(id, blogEditParam, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal RganUser user){
        blogService.deleteBlog(id, user);
    }

    @GetMapping("/search")
    public Page<BlogSummaryDTO> search(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "username", required = false) String username,
        @PageableDefault Pageable pageable){
        if(!StringUtils.isEmpty(keyword)){
            String[] keywords = keyword.split(" ");

            return blogService.searchPublishedBlogsByKeywordOrderByCreatedTimeDesc(Arrays.asList(keywords), pageable);
        }else if(!StringUtils.isEmpty(username)){
            return blogService.searchPublishedBlogsByUsernameOrderByCreatedTimeDesc(username, pageable);
        }

        return blogService.getAllPublishedBlogsOrderByCreatedTimeDesc(pageable);
    }

    @PostMapping("/{id}/favourite")
    public void addBlogToUserFavouriteList(@PathVariable Long id, @AuthenticationPrincipal RganUser user){
        userFavouriteListService.addBlogToUserFavouriteList(id, user);
    }

    @DeleteMapping("/{id}/favourite")
    public void deleteBlogFromUserFavouriteList(@PathVariable Long id, @AuthenticationPrincipal RganUser user){
        userFavouriteListService.deleteBlogFromUserFavouriteList(id, user);
    }
}
