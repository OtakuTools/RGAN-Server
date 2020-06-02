package com.okatu.rgan.blog.service;

import com.okatu.rgan.blog.model.CommentSummaryDTO;
import com.okatu.rgan.blog.repository.CommentRepository;
import com.okatu.rgan.common.exception.InvalidRequestParameterException;
import com.okatu.rgan.common.exception.ResourceAccessDeniedException;
import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.user.model.RganUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Page<CommentSummaryDTO> getAuthorAllCommentsOrderByCreatedTimeDesc(RganUser author, Pageable pageable){
        return commentRepository.findByAuthorOrderByCreatedTimeDesc(author, pageable).map(CommentSummaryDTO::convertFrom);
    }
}
