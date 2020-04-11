package com.okatu.rgan.vote.service;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.model.entity.Comment;
import com.okatu.rgan.common.exception.ConstraintViolationException;
import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.common.exception.UniquenessViolationException;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.vote.constant.VoteStatus;
import com.okatu.rgan.vote.model.VoteAbleEntity;
import com.okatu.rgan.vote.model.entity.BlogVoteItem;
import com.okatu.rgan.vote.model.entity.CommentVoteItem;
import com.okatu.rgan.vote.model.entity.VoteItem;
import com.okatu.rgan.vote.model.event.VotePublishEvent;
import com.okatu.rgan.vote.repository.BlogVoteItemRepository;
import com.okatu.rgan.vote.repository.CommentVoteItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// extremely ugly code
@Component
public class VoteService {

    @Autowired
    private BlogVoteItemRepository blogVoteItemRepository;

    @Autowired
    private CommentVoteItemRepository commentVoteItemRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public <T extends VoteAbleEntity> void doVote(T entity, RganUser user, int newStatus, @NonNull Class<T> entityClass){
        Optional optional;
        VoteItem voteItem;

        if(entityClass.equals(Blog.class)){
            optional = blogVoteItemRepository.findByBlogAndAuthor((Blog) entity, user);
            if(optional.isPresent()){
                voteItem = (VoteItem) optional.get();
                applyStateTransition(voteItem, newStatus);
            }else{
                if(newStatus == VoteStatus.CANCELED){
                    throw new ResourceNotFoundException("You cannot cancel a not exist vote!");
                }

                voteItem = new BlogVoteItem(user, (Blog) entity);
                applyOnCancelOrNotExistStatus(voteItem, newStatus);
            }
            blogVoteItemRepository.save((BlogVoteItem) voteItem);
        }else if(entityClass.equals(Comment.class)){
            optional = commentVoteItemRepository.findByCommentAndAuthor((Comment) entity, user);
            if(optional.isPresent()){
                voteItem = (VoteItem) optional.get();
                applyStateTransition(voteItem, newStatus);
            }else{
                if(newStatus == VoteStatus.CANCELED){
                    throw new ResourceNotFoundException("You cannot cancel a not exist vote!");
                }

                voteItem = new CommentVoteItem(user, (Comment) entity);
                applyOnCancelOrNotExistStatus(voteItem, newStatus);
            }
            commentVoteItemRepository.save((CommentVoteItem) voteItem);
        }else{
            throw new ConstraintViolationException("Not support, should not hit here");
        }

        if(newStatus == VoteStatus.UPVOTE){
            applicationEventPublisher.publishEvent(new VotePublishEvent(this, voteItem));
        }
    }

    private void applyStateTransition(VoteItem voteItem, int newStatus){
        if(voteItem.getStatus().equals(newStatus)){
            throw new UniquenessViolationException("You cannot re-vote/downvote/cancel the same blog!");
        }

        switch (voteItem.getStatus()){
            case VoteStatus.DOWNVOTE:
                applyOnDownVoteStatus(voteItem, newStatus);
                break;
            case VoteStatus.CANCELED:
                applyOnCancelOrNotExistStatus(voteItem, newStatus);
                break;
            case VoteStatus.UPVOTE:
                applyOnUpVoteStatus(voteItem, newStatus);
                break;
        }
    }

    private void applyOnCancelOrNotExistStatus(@NonNull VoteItem voteItem, int newStatus){
        VoteAbleEntity voteAble = voteItem.getAssociateVoteAbleEntity();
        switch (newStatus){
            case VoteStatus.DOWNVOTE:
                voteItem.setStatus(VoteStatus.DOWNVOTE);
                voteAble.decrVoteCount(1);
                break;
            case VoteStatus.UPVOTE:
                voteItem.setStatus(VoteStatus.UPVOTE);
                voteAble.incrVoteCount(1);
                break;
            default:
                throw new ConstraintViolationException("should not hit here");
        }
    }

    private void applyOnUpVoteStatus(@NonNull VoteItem voteItem, int newStatus){
        VoteAbleEntity voteAble = voteItem.getAssociateVoteAbleEntity();
        switch (newStatus){
            case VoteStatus.DOWNVOTE:
                voteItem.setStatus(VoteStatus.DOWNVOTE);
                voteAble.decrVoteCount(2);
                break;
            case VoteStatus.CANCELED:
                voteItem.setStatus(VoteStatus.CANCELED);
                voteAble.decrVoteCount(1);
                break;
            default:
                throw new ConstraintViolationException("should not hit here");
        }
    }

    private void applyOnDownVoteStatus(@NonNull VoteItem voteItem, int newStatus){
        VoteAbleEntity voteAble = voteItem.getAssociateVoteAbleEntity();
        switch (newStatus){
            case VoteStatus.CANCELED:
                voteItem.setStatus(VoteStatus.CANCELED);
                voteAble.incrVoteCount(1);
                break;
            case VoteStatus.UPVOTE:
                voteItem.setStatus(VoteStatus.UPVOTE);
                voteAble.incrVoteCount(2);
                break;
            default:
                throw new ConstraintViolationException("should not hit here");
        }
    }
}
