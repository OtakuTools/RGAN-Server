package com.okatu.rgan.blog.controller;

import com.okatu.rgan.blog.model.param.TagEditParam;
import com.okatu.rgan.blog.model.entity.Tag;
import com.okatu.rgan.common.exception.EntityNotFoundException;
import com.okatu.rgan.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    Page<Tag> all(@PageableDefault Pageable pageable){
        return tagRepository.findAll(pageable);
    }

    @PostMapping
    // perform parameter validation
    Tag add(@Valid @RequestBody TagEditParam tagEditParam){
        Tag tag = new Tag(tagEditParam.getTitle(), tagEditParam.getDescription());
        return tagRepository.save(tag);
    }

    @GetMapping("/{id}")
    Tag one(@PathVariable Long id){
        return tagRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("tag", id));
    }

    @PutMapping("/{id}")
    Tag edit(@PathVariable Long id, @RequestBody TagEditParam tagEditParam){
        return tagRepository.findById(id).map(
                tag -> {
                    tag.setTitle(tagEditParam.getTitle());
                    tag.setDescription(tagEditParam.getDescription());
                    return tagRepository.save(tag);
                }
        ).orElseThrow(() -> new EntityNotFoundException("tag", id));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id){
        tagRepository.deleteById(id);
    }
}
