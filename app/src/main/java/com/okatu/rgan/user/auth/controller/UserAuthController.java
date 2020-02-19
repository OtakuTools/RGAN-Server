package com.okatu.rgan.user.auth.controller;

import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.UserRegisterParam;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public String register(@Valid @RequestBody UserRegisterParam userRegisterParam){

        if(userRepository.findByUsername(userRegisterParam.getUsername()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username has been register");
        }

        RganUser user = new RganUser();
        user.setUsername(userRegisterParam.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterParam.getPassword()));
        // some exception will be thrown if this procedure fail
        userRepository.save(user);

        return "register success";
    }
}
