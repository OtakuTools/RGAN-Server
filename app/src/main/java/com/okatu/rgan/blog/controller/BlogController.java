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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
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
    List<Blog> all(){
        return blogRepository.findAll();
    }

//    @GetMapping
//    Page<Blog> getPage(@PageableDefault Pageable pageable){
//        return blogRepository.findAll(pageable);
//    }

    @PostMapping
    BlogDTO add(@RequestBody BlogEditParam blogEditParam, @AuthenticationPrincipal RganUser user){
        LinkedHashSet<Tag> tags = findTagsByTitles(blogEditParam.getTags());
        Blog blog = new Blog();
        blog.setTitle(blogEditParam.getTitle());
        blog.setContent(blogEditParam.getContent());
        blog.setTags(tags);
        blog.setUser(userRepository.findByUsername(user.getUsername())
            .orElseThrow(() -> new ConstraintViolationException("Cannot find current request username in repository, username: " + user.getUsername()))
        );

        return BlogDTO.convertFrom(blogRepository.save(blog));
    }

    @GetMapping("/{id}")
    BlogDTO one(@PathVariable Long id){
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

    private LinkedHashSet<Tag> findTagsByTitles(LinkedHashSet<String> titles){
        return titles.stream().map(
            title -> tagRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Could not find Tag, title: " + title))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
