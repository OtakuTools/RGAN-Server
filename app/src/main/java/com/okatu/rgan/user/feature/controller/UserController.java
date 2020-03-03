package com.okatu.rgan.user.feature.controller;

import com.okatu.rgan.common.exception.EntityNotFoundException;
import com.okatu.rgan.user.feature.model.param.UserProfileEditParam;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.RganUserDTO;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// what do you need:
// user follow update
// register hook event
// when post, publish to all followed
// how receive?
// redis?
// sort by createdTime
// id -> message queue
// keep a 'hot' list(fit size)
// if you want more, turn to db
// consistency?
//
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public RganUserDTO getUserInfo(@PathVariable Long id){
        return userRepository.findById(id).map(RganUserDTO::convertFrom)
            .orElseThrow(() -> new EntityNotFoundException("user", id));
    }

    @PutMapping("/{id}/profile")
    public RganUserDTO editUserProfile(@PathVariable Long id, @RequestBody UserProfileEditParam userProfileEditParam,
                                       @AuthenticationPrincipal RganUser user){
        return RganUserDTO.convertFrom(userRepository.save(user));
    }
}
