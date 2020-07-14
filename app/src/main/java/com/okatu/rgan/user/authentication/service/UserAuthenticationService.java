package com.okatu.rgan.user.authentication.service;

import com.okatu.rgan.user.authentication.model.param.UserRegisterParam;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.RganUserDTO;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public RganUserDTO register(UserRegisterParam userRegisterParam){
        if(userRepository.findByUsername(userRegisterParam.getUsername()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username has been registered");
        }

        RganUser user = new RganUser();
        user.setUsername(userRegisterParam.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterParam.getPassword()));
        return RganUserDTO.convertFrom(userRepository.save(user));
    }
}
