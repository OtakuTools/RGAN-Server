package com.okatu.rgan.blog.service;

import com.okatu.rgan.blog.model.CommentSummaryDTO;
import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.model.event.CommentPublishEvent;
import com.okatu.rgan.blog.model.param.CommentEditParam;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.blog.repository.CommentRepository;
import com.okatu.rgan.common.exception.InvalidRequestParameterException;
import com.okatu.rgan.common.exception.ResourceAccessDeniedException;
import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.user.model.RganUser;

import com.okatu.rgan.vote.model.entity.CommentVoteCounter;
import com.okatu.rgan.vote.repository.CommentVoteCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Component
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentVoteCounterRepository commentVoteCounterRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Page<CommentSummaryDTO> getAuthorAllCommentsOrderByCreatedTimeDesc(RganUser author, Pageable pageable){
        return commentRepository.findByAuthorOrderByCreatedTimeDesc(author, pageable).map(CommentSummaryDTO::convertFrom);
    }

    @Transactional
    public Comment addComment(long blogId, CommentEditParam commentEditParam, @NonNull RganUser user){
        Comment comment = new Comment();
        comment.setContent(commentEditParam.getContent());
        comment.setAuthor(user);
        comment.setBlog(blogRepository.findById(blogId).orElseThrow(
            () -> new ResourceNotFoundException("blog", blogId)
        ));

        if(commentEditParam.getReplyTo() != null){
            comment.setReplyTo(commentRepository.findById(commentEditParam.getReplyTo())
                .orElseThrow(
                    () -> new ResourceNotFoundException("comment", commentEditParam.getReplyTo())
                ));
        }

        Comment saved = commentRepository.save(comment);
        CommentVoteCounter commentVoteCounter = new CommentVoteCounter(saved);
        saved.setVoteCounter(commentVoteCounterRepository.save(commentVoteCounter));
        eventPublisher.publishEvent(new CommentPublishEvent(this, comment));
        return saved;
    }
}
