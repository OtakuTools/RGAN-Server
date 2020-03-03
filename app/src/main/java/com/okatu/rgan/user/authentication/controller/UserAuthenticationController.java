package com.okatu.rgan.user.authentication.controller;

import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.RganUserDTO;
import com.okatu.rgan.user.authentication.model.param.UserRegisterParam;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class UserAuthenticationController {

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


}
