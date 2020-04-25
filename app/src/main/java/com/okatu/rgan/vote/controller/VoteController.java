package com.okatu.rgan.vote.controller;

import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.repository.CommentRepository;
import com.okatu.rgan.common.exception.ConstraintViolationException;
import com.okatu.rgan.vote.constant.VoteStatus;
import com.okatu.rgan.vote.model.VoteStatusDTO;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import com.okatu.rgan.vote.model.entity.CommentVoteItem;
import com.okatu.rgan.vote.model.event.VotePublishEvent;
import com.okatu.rgan.vote.model.param.VoteParam;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.vote.repository.BlogVoteItemRepository;
import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.repository.CommentVoteItemRepository;
import com.okatu.rgan.vote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class VoteController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogVoteItemRepository blogVoteItemRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentVoteItemRepository commentVoteItemRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private VoteService voteService;

    @PostMapping("/blogs/{id}/vote")
    public String voteBlog(@PathVariable("id") Long id, @RequestBody @Valid VoteParam voteParam,
                           @AuthenticationPrincipal RganUser user){
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("blog", id));

        voteService.doVote(blog, user, voteParam.getStatus(), Blog.class);
        // for display, upvote count
        // for check current request valid
        // save all the upvote item
        return "";
    }

    @PostMapping("/comments/{id}/vote")
    public String voteComment(@PathVariable("id") Long id, @RequestBody @Valid VoteParam voteParam,
                              @AuthenticationPrincipal RganUser user){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("blog", id));

        voteService.doVote(comment, user, voteParam.getStatus(), Comment.class);

        return "";
    }

    @GetMapping("/blogs/vote/status")
    public List<VoteStatusDTO> selfBlogVoteStatus(@RequestParam("id[]") List<Long> blogIds, @AuthenticationPrincipal RganUser user){
        return blogVoteItemRepository.findByBlog_IdInAndAuthor(blogIds, user).stream().map(VoteStatusDTO::convertFrom).collect(Collectors.toList());
    }

    @GetMapping("/comments/vote/status")
    public List<VoteStatusDTO> selfCommentVoteStatus(@RequestParam("id[]") List<Long> commentIds, @AuthenticationPrincipal RganUser user){
        return commentVoteItemRepository.findByComment_IdInAndAuthor(commentIds, user).stream().map(VoteStatusDTO::convertFrom).collect(Collectors.toList());
    }
}
