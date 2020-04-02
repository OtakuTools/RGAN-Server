package com.okatu.rgan.user.authentication.controller;

import com.okatu.rgan.user.authentication.model.param.ChangePasswordParam;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/password")
public class UserPasswordController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/change")
    public String changePassword(@RequestBody @Valid ChangePasswordParam param,
                                 @AuthenticationPrincipal RganUser user){

        String encodedPassword = passwordEncoder.encode(param.getPassword());
        if(encodedPassword.equals(user.getPassword())){
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong origin password");
        }

        // TODO: dummy here
        return "";
    }

    @PostMapping("/retrieve/verification")
    public String dummy(){
        return "";
    }

    @PostMapping("/retrieve/confirm")
    public String confirm(){
        return "";
    }
}
