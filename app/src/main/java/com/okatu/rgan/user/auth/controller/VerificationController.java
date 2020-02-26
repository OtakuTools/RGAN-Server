package com.okatu.rgan.user.auth.controller;

import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.UserVerification;
import com.okatu.rgan.user.model.param.ReceiveUserVerificationParam;
import com.okatu.rgan.user.model.param.SendVerificationEmailParam;
import com.okatu.rgan.user.repository.UserRepository;
import com.okatu.rgan.user.repository.UserVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;


// state transition, see:
// https://www.yuque.com/fugi8p/vawoon/ghaqgf
@RestController
@RequestMapping("/verification")
public class VerificationController {

    private static int MINIMUM_REQUEST_INTERVAL = 1000;

    private static int EXPIRED_INTERVAL_IN_MINUTE = 15;

    @Autowired
    private UserVerificationRepository userVerificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    @PostMapping("/email/send")
    public String sendVerificationEmail(@Valid @RequestBody SendVerificationEmailParam verificationParam, @AuthenticationPrincipal RganUser user){
        Optional<UserVerification> verificationOptional = userVerificationRepository.findByUsername(user.getUsername());
        if(verificationOptional.isPresent()){
            UserVerification userVerification = verificationOptional.get();

            switch (userVerification.getStatus()){
                case UserVerification.CREATED:{
                    // time limit check
                    // in fact, should not be controlled by db, all the pressure will hit db
                    if(Duration.between(userVerification.getCreatedTime(), LocalDateTime.now()).abs().toMillis()
                        > MINIMUM_REQUEST_INTERVAL){
                        setCreatedVerificationStatus(userVerification);
                        userVerificationRepository.save(userVerification);
                        sendVerificationEmail(userVerification);
                    }else{
                        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too Frequent request");
                    }
                    break;
                }
                case UserVerification.EXPIRED:{
                    setCreatedVerificationStatus(userVerification);
                    userVerificationRepository.save(userVerification);
                    sendVerificationEmail(userVerification);
                    break;
                }
                case UserVerification.VERIFIED:{
                    if(verificationParam.getEmail().equals(userVerification.getEmail())){
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This email address had been verified");
                    }else{
                        setCreatedVerificationStatus(userVerification);
                        userVerificationRepository.save(userVerification);
                        sendVerificationEmail(userVerification);
                    }
                    break;
                }
            }
        }else{
            UserVerification userVerification = new UserVerification();
            setCreatedVerificationStatus(userVerification);
            userVerification.setEmail(verificationParam.getEmail());
            userVerification.setUsername(user.getUsername());
            userVerificationRepository.save(userVerification);
            sendVerificationEmail(userVerification);
        }

        // TODO: better return value
        return "";
    }

    @PostMapping("email/receive")
//    @GetMapping("email/receive")
    public String receiveVerificationToken(@RequestBody ReceiveUserVerificationParam userVerificationParam){
        Optional<UserVerification> verificationOptional = userVerificationRepository.findByToken(userVerificationParam.getToken());

        if(verificationOptional.isPresent()){
            UserVerification userVerification = verificationOptional.get();
            switch (userVerification.getStatus()){
                case UserVerification.CREATED:{
                    // if the rhs is before the lhs, negative result
                    if(Duration.between(userVerification.getCreatedTime(), LocalDateTime.now()).toMinutes() < EXPIRED_INTERVAL_IN_MINUTE){
                        userVerification.setStatus(UserVerification.VERIFIED);
                        // update token status
                        userVerificationRepository.save(userVerification);
                        userRepository.findByUsername(userVerification.getUsername()).ifPresent(user -> {
                            user.setEmail(userVerification.getEmail());
                            // write to user info
                            userRepository.save(user);
                        });
                    }else{
                        userVerification.setStatus(UserVerification.EXPIRED);
                        userVerificationRepository.save(userVerification);
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This token is expired");
                    }
                    break;
                }
                case UserVerification.EXPIRED:{
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This token is expired");
                }
                case UserVerification.VERIFIED:{
                    // do nothing here
                    break;
                }
            }
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bad verification request");
        }

        // TODO: better return value
        return "";
    }

    private void setCreatedVerificationStatus(UserVerification userVerification){
        userVerification.setCreatedTime(LocalDateTime.now());
        userVerification.setStatus(UserVerification.CREATED);
        userVerification.setToken(UUID.randomUUID().toString());
    }

    private void sendVerificationEmail(UserVerification userVerification){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(userVerification.getEmail());
        simpleMailMessage.setSubject("Rgan Email Verification");

        String verificationLink = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/verification").path("/email").path("/receive")
            .queryParam("token", userVerification.getToken()).build().toUriString();

        simpleMailMessage.setText(verificationLink);

        javaMailSender.send(simpleMailMessage);
    }
}
