package com.okatu.rgan.blog.service;

import com.okatu.rgan.blog.model.CommentSummaryDTO;
import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.blog.model.event.CommentDeleteEvent;
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
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentService {

    private final static int MAXIMUM_COMMENT_EDIT_INTERVAL = 5 * 60 * 1000;

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

    public List<CommentSummaryDTO> getBlogAllComments(long blogId){
        return commentRepository.findByBlog(blogRepository.getOne(blogId)).stream().map(CommentSummaryDTO::convertFrom).collect(Collectors.toList());
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

    public Comment editComment(long commentId, @NonNull CommentEditParam commentEditParam, @NonNull RganUser user){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment", commentId));

        if(RganUser.isNotSame(comment.getAuthor(), user)){
            throw new ResourceAccessDeniedException("you have no permission to edit this blog");
        }

        if(Duration.between(comment.getCreatedTime(), LocalDateTime.now()).toMillis() > MAXIMUM_COMMENT_EDIT_INTERVAL){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only edit comment within 5 minutes");
        }

        comment.setContent(commentEditParam.getContent());
        comment.setModifiedTime(LocalDateTime.now());
        // ignore the reply to

        return commentRepository.save(comment);
    }

    public void deleteComment(long commentId, @NonNull RganUser user){
        commentRepository.findById(commentId).ifPresent(comment -> {
            if(RganUser.isNotSame(comment.getAuthor(), user)){
                throw new ResourceAccessDeniedException("you have no permission to delete this blog");
            }

            commentRepository.deleteById(commentId);

            eventPublisher.publishEvent(new CommentDeleteEvent(this, commentId));
        });
    }
}
