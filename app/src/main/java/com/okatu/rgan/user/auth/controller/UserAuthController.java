package com.okatu.rgan.user.auth.controller;

import com.okatu.rgan.common.exception.EntityNotFoundException;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.RganUserDTO;
import com.okatu.rgan.user.model.UserRegisterParam;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class UserAuthController {

    @Autowired
    private UserRepository userRepository;

    // BCrypt, see the SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    // TODO: better return value?
    @PostMapping("/register")
    public RganUserDTO register(@Valid @RequestBody UserRegisterParam userRegisterParam){

        if(userRepository.findByUsername(userRegisterParam.getUsername()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username has been registered");
        }

        RganUser user = new RganUser();
        user.setUsername(userRegisterParam.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterParam.getPassword()));
        return RganUserDTO.convertFrom(userRepository.save(user));
    }

    @GetMapping("/users/{id}")
    public RganUserDTO getUserInfo(@PathVariable Long id){
        return userRepository.findById(id).map(RganUserDTO::convertFrom)
            .orElseThrow(() -> new EntityNotFoundException("user", id));
    }
}
