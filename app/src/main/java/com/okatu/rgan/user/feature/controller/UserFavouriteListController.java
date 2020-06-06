package com.okatu.rgan.user.feature.controller;

import com.okatu.rgan.blog.model.entity.Blog;
import com.okatu.rgan.user.feature.model.UserFavouriteListDTO;
import com.okatu.rgan.user.feature.service.UserFavouriteListService;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserFavouriteListController {

    @Autowired
    private UserFavouriteListService userFavouriteListService;

    @GetMapping("/users/self/favourite")
    public Page<UserFavouriteListDTO> getUserFavouriteList(@PageableDefault Pageable pageable, @AuthenticationPrincipal RganUser user){
        return userFavouriteListService.getUserFavouriteList(user, pageable);
    }
}
