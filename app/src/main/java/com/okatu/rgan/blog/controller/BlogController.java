package com.okatu.rgan.blog.controller;

import com.okatu.rgan.common.exception.ConstraintViolationException;
import com.okatu.rgan.common.exception.ResourceAccessDeniedException;
import com.okatu.rgan.blog.model.BlogDTO;
import com.okatu.rgan.blog.model.param.BlogEditParam;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Tag;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.common.exception.EntityNotFoundException;
import com.okatu.rgan.blog.repository.TagRepository;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    @GetMapping
    List<BlogDTO> all(){
        return blogRepository.findAll().stream().map(BlogDTO::convertFrom).collect(Collectors.toList());
    }

//    @GetMapping
//    Page<Blog> getPage(@PageableDefault Pageable pageable){
//        return blogRepository.findAll(pageable);
//    }

    @PostMapping
    BlogDTO add(@RequestBody BlogEditParam blogEditParam, @AuthenticationPrincipal RganUser user){
        LinkedHashSet<Tag> tags = findTagsByTitles(blogEditParam.getTags());
        LocalDateTime now = LocalDateTime.now();
        Blog blog = new Blog();
        blog.setTitle(blogEditParam.getTitle());
        blog.setContent(blogEditParam.getContent());
        blog.setTags(tags);
        blog.setUser(userRepository.findByUsername(user.getUsername())
            .orElseThrow(() -> new ConstraintViolationException("Cannot find current request username in repository, username: " + user.getUsername()))
        );

        return BlogDTO.convertFrom(blogRepository.save(blog));
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
    BlogDTO one(@PathVariable Long id, HttpSession session){
        return blogRepository.findById(id).map(BlogDTO::convertFrom)
            .orElseThrow(() -> new EntityNotFoundException("blog", id));
    }

    @PutMapping("/{id}")
    BlogDTO edit(@PathVariable Long id, @RequestBody BlogEditParam blogEditParam, @AuthenticationPrincipal RganUser user){

        Blog blog = blogRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("blog", id));

        if(!blog.getUser().getId().equals(user.getId())){
            throw new ResourceAccessDeniedException("you have no permission to edit this blog");
        }

        blog.setTitle(blogEditParam.getTitle());
        blog.setContent(blogEditParam.getContent());
        blog.setTags(findTagsByTitles(blogEditParam.getTags()));

        return BlogDTO.convertFrom(blogRepository.save(blog));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @AuthenticationPrincipal RganUser user){
        blogRepository.findById(id).ifPresent(blog -> {
            if(!blog.getUser().getId().equals(user.getId())){
                throw new ResourceAccessException("you have no permission to delete this blog");
            }
            blogRepository.deleteById(id);
        });
    }

    @GetMapping("/search")
    List<BlogDTO> search(@RequestParam("keyword") String keyword){
        if(StringUtils.isEmpty(keyword)){
            return blogRepository.findAll().stream().map(BlogDTO::convertFrom)
                .collect(Collectors.toList());
        }

        String[] keywords = keyword.split(" ");

        return blogRepository.findByTitleContainsAnyOfKeywords(Arrays.asList(keywords)).stream().map(BlogDTO::convertFrom).collect(Collectors.toList());
    }

    @PostMapping("/{id}/comments")
    public void comment(){

    }

    // upvote Request URL: https://stackoverflow.com/posts/2913160/vote/2
    // downvote Request URL: https://stackoverflow.com/posts/2913160/vote/3
    @PostMapping("/{id}/vote")
    public void vote(){

    }

    private LinkedHashSet<Tag> findTagsByTitles(LinkedHashSet<String> titles){
        return titles.stream().map(
            title -> tagRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Could not find Tag, title: " + title))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
