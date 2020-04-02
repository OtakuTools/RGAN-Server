package com.okatu.rgan.blog.controller;

import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.common.exception.EntityNotFoundException;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoteController {

    @Autowired
    private BlogRepository blogRepository;

    @PostMapping("blogs/{id}/vote")
    public String vote(@PathVariable("id") Long id, @AuthenticationPrincipal RganUser user){
        if(!blogRepository.findById(id).isPresent()){
            throw new EntityNotFoundException("blog", id);
        }

        // for display, upvote count
        // for check current request valid
        // save all the upvote item
        // how to handle inconsistency/vote miss?
        return "";
    }

//    @DeleteMapping("")
}
