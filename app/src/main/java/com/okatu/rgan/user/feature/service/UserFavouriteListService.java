package com.okatu.rgan.user.feature.service;

import com.okatu.rgan.blog.constant.BlogStatus;
import com.okatu.rgan.blog.model.BlogDTO;
import com.okatu.rgan.blog.model.BlogSummaryDTO;
import com.okatu.rgan.blog.model.TagSummaryDTO;
import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.blog.repository.BlogRepository;
import com.okatu.rgan.common.exception.ResourceNotFoundException;
import com.okatu.rgan.user.feature.model.UserFavouriteListDTO;
import com.okatu.rgan.user.feature.model.entity.UserFavouriteList;
import com.okatu.rgan.user.feature.model.entity.UserFavouriteListId;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.UserFavouriteListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserFavouriteListService {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserFavouriteListRepository userFavouriteListRepository;

    public void addBlogToUserFavouriteList(long blogId, @NonNull RganUser user){
        Blog blog = blogRepository.findByIdAndStatus(blogId, BlogStatus.PUBLISHED)
            .orElseThrow(() -> new ResourceNotFoundException("blog", blogId));

        UserFavouriteListId userFavouriteListId = new UserFavouriteListId(blog, user);
        Optional<UserFavouriteList> optional = userFavouriteListRepository.findById(userFavouriteListId);

        if(optional.isPresent()){
            UserFavouriteList userFavouriteList = optional.get();

            if(userFavouriteList.isEnabled()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have been following this user!");
            }else{
                userFavouriteList.setEnabled(true);
                userFavouriteListRepository.save(userFavouriteList);
            }
        }else{
            UserFavouriteList userFavouriteList = new UserFavouriteList(userFavouriteListId);
            userFavouriteListRepository.save(userFavouriteList);
        }
    }

    public void deleteBlogFromUserFavouriteList(long blogId, @NonNull RganUser user){
        UserFavouriteListId userFavouriteListId = new UserFavouriteListId(blogRepository.getOne(blogId), user);
        userFavouriteListRepository.findByIdAndEnabledTrue(userFavouriteListId).ifPresent(userFavouriteList -> {
            userFavouriteList.setEnabled(false);
            userFavouriteListRepository.save(userFavouriteList);
        });
    }

    public Page<UserFavouriteListDTO> getUserFavouriteList(@NonNull RganUser user, Pageable pageable){
        return userFavouriteListRepository.findById_UserAndEnabledTrueOrderByCreatedTimeDesc(user, pageable)
            .map(userFavouriteList -> {
                Blog blog = userFavouriteList.getId().getBlog();

                UserFavouriteListDTO userFavouriteListDTO = new UserFavouriteListDTO();
                userFavouriteListDTO.setEnlistTime(userFavouriteList.getCreatedTime());
                userFavouriteListDTO.setBlogSummaryDTO(blog.getStatus().equals(BlogStatus.PUBLISHED) ? BlogSummaryDTO.convertFrom(blog) : null);
                return userFavouriteListDTO;
            });
    }
}
