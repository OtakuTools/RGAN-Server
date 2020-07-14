package com.okatu.rgan.user.authentication.controller;

import com.okatu.rgan.common.exception.VerificationEmailUnavailableException;
import com.okatu.rgan.user.authentication.constant.UserVerificationStatus;
import com.okatu.rgan.user.authentication.service.VerificationService;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.authentication.model.param.ReceiveUserVerificationParam;
import com.okatu.rgan.user.authentication.model.param.SendVerificationEmailParam;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


// state transition, see:
// https://www.yuque.com/fugi8p/vawoon/ghaqgf
// user -> userVerification
// fail -> success is impossible
// fail -> fail whatever
// success -> fail is what we need to consider
// transactional
// userVerification -> user
// user write fail
// save all the verification message?
// consider situation like:
// user A already registered abc@123.com
// A re-register def@123.com, the user_verification status -> def@123.com, user status remain -> abc@123.com
// user B register abc@123.com
// if only check user_verification table
// user B register success, that will remain two entity in user table both have email -> abc@123.com
// so we need to store all the verification message?
// however, transaction need here

// what if we combine user and user_verification table together?
// consider situation like:
// user A re-register

// consider situation like: user A reg
@RestController
@RequestMapping("/verification")
public class VerificationController {
    @Autowired
    private VerificationService verificationService;
//
//    @PostMapping("/code/send")
//    public String sendVerificationCode(){
//
//    }
//
//    @PostMapping("/code/receive")
//    public String receiveVerificationCode(){
//
//    }

    @PostMapping("/email/send")
    public String sendVerificationEmail(@Valid @RequestBody SendVerificationEmailParam verificationParam, @AuthenticationPrincipal RganUser user){
        return verificationService.sendVerificationEmail(verificationParam, user);
        // TODO: better return value
    }

    @PostMapping("email/receive")
    public String receiveVerificationToken(@RequestBody ReceiveUserVerificationParam userVerificationParam){
        return verificationService.receiveVerificationToken(userVerificationParam);
    }
}
