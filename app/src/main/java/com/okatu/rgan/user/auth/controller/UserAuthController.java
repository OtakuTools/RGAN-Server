package com.okatu.rgan.user.auth.controller;

import com.okatu.rgan.common.exception.EntityNotFoundException;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.RganUserDTO;
import com.okatu.rgan.user.model.UserVerification;
import com.okatu.rgan.user.model.param.SendVerificationEmailParam;
import com.okatu.rgan.user.model.param.UserRegisterParam;
import com.okatu.rgan.user.model.param.ReceiveUserVerificationParam;
import com.okatu.rgan.user.repository.UserRepository;
import com.okatu.rgan.user.repository.UserVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
