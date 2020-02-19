package com.okatu.rgan.blog.controller;

import com.okatu.rgan.blog.model.BlogEditParam;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Tag;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.blog.exception.EntityNotFoundException;
import com.okatu.rgan.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    List<Blog> all(){
        return blogRepository.findAll();
    }

//    @GetMapping
//    Page<Blog> getPage(@PageableDefault Pageable pageable){
//        return blogRepository.findAll(pageable);
//    }

    @PostMapping
    Blog add(@RequestBody BlogEditParam blogEditParam){
        LinkedHashSet<Tag> tags = findTagsByTitles(blogEditParam.getTags());
        Blog blog = new Blog();
        blog.setTitle(blogEditParam.getTitle());
        blog.setContent(blogEditParam.getContent());
        blog.setTags(tags);
        return blogRepository.save(blog);
    }

    @GetMapping("/{id}")
    Blog one(@PathVariable Long id){
        return blogRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("blog", id));
    }

    @PutMapping("/{id}")
    Blog edit(@PathVariable Long id, @RequestBody BlogEditParam blogEditParam){

        return blogRepository.findById(id).map(
            blog -> {
                blog.setTitle(blogEditParam.getTitle());
                blog.setContent(blogEditParam.getContent());
                blog.setTags(findTagsByTitles(blogEditParam.getTags()));
                return blogRepository.save(blog);
            }
        ).orElseThrow(() -> new EntityNotFoundException("blog", id));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id){
        blogRepository.deleteById(id);
    }

    private LinkedHashSet<Tag> findTagsByTitles(LinkedHashSet<String> titles){
        return titles.stream().map(
            title -> tagRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Could not find Tag, title: " + title))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
