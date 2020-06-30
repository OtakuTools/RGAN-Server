package com.okatu.rgan.blog.controller;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.model.param.CommentEditParam;
import com.okatu.rgan.blog.model.CommentSummaryDTO;
import com.okatu.rgan.blog.service.CommentService;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/blogs")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{blogId}/comments")
    public List<CommentSummaryDTO> all(@PathVariable("blogId") Long blogId){
        return commentService.getBlogAllComments(blogId);
    }

    // about getOne and findById:
    // https://www.javacodemonk.com/difference-between-getone-and-findbyid-in-spring-data-jpa-3a96c3ff
    // Request URL: https://stackoverflow.com/posts/60629950/comments
    @PostMapping("/{blogId}/comments")
    public String add(@PathVariable("blogId") Long blogId, @RequestBody @Valid CommentEditParam commentEditParam, @AuthenticationPrincipal RganUser user){
        Comment comment = commentService.addComment(blogId, commentEditParam, user);

        return "";
    }

    @PutMapping("/comments/{commentId}")
    public String edit(@PathVariable("commentId") Long commentId,
                       @RequestBody @Valid CommentEditParam commentEditParam, @AuthenticationPrincipal RganUser user){
        Comment comment = commentService.editComment(commentId, commentEditParam, user);

        return "";
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal RganUser user){
        commentService.deleteComment(commentId, user);
    }

//    // upvote Request URL: https://stackoverflow.com/posts/2913160/vote/2
//    // downvote Request URL: https://stackoverflow.com/posts/2913160/vote/3
//    @PostMapping("/{id}/vote")
//    public void vote(){
//
//    }
}
