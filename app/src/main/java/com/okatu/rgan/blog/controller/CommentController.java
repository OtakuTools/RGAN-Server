package com.okatu.rgan.blog.controller;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.model.event.CommentPublishEvent;
import com.okatu.rgan.blog.model.param.CommentEditParam;
import com.okatu.rgan.blog.model.CommentSummaryDTO;
import com.okatu.rgan.blog.model.projection.CommentSummaryProjection;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.blog.repository.CommentRepository;
import com.okatu.rgan.common.exception.EntityNotFoundException;
import com.okatu.rgan.common.exception.ResourceAccessDeniedException;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/blogs")
public class CommentController {

    private final static int MAXIMUM_COMMENT_EDIT_INTERVAL = 5 * 60 * 1000;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/{blogId}/comments")
    public Page<CommentSummaryDTO> all(@PathVariable("blogId") Long blogId, @PageableDefault(size = 20) Pageable pageable){
        Page<CommentSummaryProjection> commentSummaryProjections = commentRepository.findByBlog_Id(blogId, pageable);
        return commentRepository.findByBlog_Id(blogId, pageable).map(CommentSummaryDTO::convertFrom);
    }

    // about getOne and findById:
    // https://www.javacodemonk.com/difference-between-getone-and-findbyid-in-spring-data-jpa-3a96c3ff
    // Request URL: https://stackoverflow.com/posts/60629950/comments
    @PostMapping("/{blogId}/comments")
    public String add(@PathVariable("blogId") Long blogId, @RequestBody CommentEditParam commentEditParam, @AuthenticationPrincipal RganUser user){
        Comment comment = new Comment();
        comment.setContent(commentEditParam.getContent());
        comment.setAuthor(user);
        comment.setBlog(blogRepository.findById(blogId).orElseThrow(
            () -> new EntityNotFoundException("blog", blogId)
        ));

        if(commentEditParam.getReplyTo() != null){
            comment.setReplyTo(userRepository.findById(commentEditParam.getReplyTo())
                .orElseThrow(
                    () -> new EntityNotFoundException("user", commentEditParam.getReplyTo())
                ));
        }

        commentRepository.save(comment);
        eventPublisher.publishEvent(new CommentPublishEvent(this, comment));

        return "";
    }

    @PutMapping("/comments/{commentId}")
    public String edit(@PathVariable("commentId") Long commentId,
                       @RequestBody CommentEditParam commentEditParam, @AuthenticationPrincipal RganUser user){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("comment", commentId));

        if(!RganUser.isSame(comment.getAuthor(), user)){
            throw new ResourceAccessDeniedException("you have no permission to edit this blog");
        }

        if(Duration.between(comment.getCreatedTime(), LocalDateTime.now()).toMillis() > MAXIMUM_COMMENT_EDIT_INTERVAL){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only edit comment within 5 minutes");
        }

        comment.setContent(commentEditParam.getContent());
        comment.setAuthor(user);

        if(commentEditParam.getReplyTo() != null){
            comment.setReplyTo(
                userRepository.findById(commentEditParam.getReplyTo())
                    .orElseThrow(() -> new EntityNotFoundException("user", commentEditParam.getReplyTo()))
            );
        }else{
            comment.setReplyTo(null);
        }

        commentRepository.save(comment);

        return "";
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal RganUser user){
        commentRepository.findById(commentId).ifPresent(comment -> {
            if(!RganUser.isSame(comment.getAuthor(), user)){
                throw new ResourceAccessDeniedException("you have no permission to delete this blog");
            }

            commentRepository.deleteById(commentId);
        });
    }

//    // upvote Request URL: https://stackoverflow.com/posts/2913160/vote/2
//    // downvote Request URL: https://stackoverflow.com/posts/2913160/vote/3
//    @PostMapping("/{id}/vote")
//    public void vote(){
//
//    }
}
