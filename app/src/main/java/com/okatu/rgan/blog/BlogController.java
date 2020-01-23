package com.okatu.rgan.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
    private final BlogRepository blogRepository;

    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping
    List<Blog> all(){
        return blogRepository.findAll();
    }

    @PostMapping
    Blog newBlog(@RequestBody Blog blog){
        return blogRepository.save(blog);
    }

    @GetMapping("/{id}")
    Blog one(@PathVariable Long id){
        return blogRepository.findById(id).
                orElseThrow(() -> new BlogNotFoundException(id));
    }

    @PutMapping("/{id}")
    Blog edit(@PathVariable Long id, @RequestBody Blog newBlog){
        return blogRepository.findById(id).map(
            blog -> {
                blog.setContent(newBlog.getContent());
                return blogRepository.save(blog);
            }
        ).orElseThrow(() -> new BlogNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    void deleteBlog(@PathVariable Long id){
        blogRepository.deleteById(id);
    }
}
