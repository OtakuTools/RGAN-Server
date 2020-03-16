package com.okatu.rgan.user.authentication.controller;

import com.okatu.rgan.common.exception.VerificationEmailUnavailableException;
import com.okatu.rgan.user.authentication.constant.UserVerificationStatus;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    private static int MINIMUM_REQUEST_INTERVAL_IN_MILLIS = 60 * 1000;

    private static int EXPIRED_INTERVAL_IN_MINUTE = 15;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    @PostMapping("/email/send")
    public String sendVerificationEmail(@Valid @RequestBody SendVerificationEmailParam verificationParam, @AuthenticationPrincipal RganUser user){

        // pre-condition check
        checkVerificationEmailAvailable(user, verificationParam.getEmail());

        switch (user.getVerificationStatus()){
            case UserVerificationStatus.NOT_EXISTED:
            case UserVerificationStatus.EXPIRED:
            case UserVerificationStatus.VERIFIED:
                createNewVerification(user, verificationParam.getEmail());
                break;
            case UserVerificationStatus.CREATED:{
                if(Duration.between(user.getVerificationCreatedTime(), LocalDateTime.now()).abs().toMillis() >
                    MINIMUM_REQUEST_INTERVAL_IN_MILLIS){
                    createNewVerification(user, verificationParam.getEmail());
                }else {
                    throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too Frequent request");
                }
                break;
            }
        }

        // TODO: better return value
        return "";
    }

    @PostMapping("email/receive")
    public String receiveVerificationToken(@RequestBody ReceiveUserVerificationParam userVerificationParam){
        Optional<RganUser> userOptional = userRepository.findByVerificationToken(userVerificationParam.getToken());

        if(userOptional.isPresent()){
            RganUser user = userOptional.get();
            switch (user.getVerificationStatus()){
                case UserVerificationStatus.CREATED:{
                    if(isVerificationDateExpired(user)){
                        user.setVerificationStatus(UserVerificationStatus.EXPIRED);
                        userRepository.save(user);
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This token is expired");
                    }else{
                        user.setVerificationStatus(UserVerificationStatus.VERIFIED);
                        user.setEmail(user.getVerificationEmail());
                        user.setVerificationToken(null);
                        user.setVerificationEmail(null);
                        userRepository.save(user);
                    }
                    break;
                }
                case UserVerificationStatus.EXPIRED:{
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This token is expired");
                }
                case UserVerificationStatus.VERIFIED:{
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

    private void checkVerificationEmailAvailable(RganUser self, String email){
        // such email must not exist:
        // not self, same email address as parameter and status == CREATED or status == VERIFIED
        List<RganUser> userVerifications = userRepository.findByEmailOrVerificationEmail(email, email);

        for(RganUser user : userVerifications){
            if(!RganUser.isSame(self, user) &&
                (user.getVerificationStatus() == UserVerificationStatus.VERIFIED || !isVerificationDateExpired(user))){
                throw new VerificationEmailUnavailableException(email);
            }
        }
    }

//    @Transactional
    private void createNewVerification(RganUser user, String verificationEmail){
        setCreatedVerificationStatus(user, verificationEmail);
        userRepository.save(user);
        sendVerificationEmail(user);
    }

    private void setCreatedVerificationStatus(RganUser user, String verificationEmail){
        user.setVerificationEmail(verificationEmail);
        user.setVerificationCreatedTime(LocalDateTime.now());
        user.setVerificationStatus(UserVerificationStatus.CREATED);
        user.setVerificationToken(UUID.randomUUID().toString());
    }

    private boolean isVerificationDateExpired(RganUser user){
        return user.getVerificationStatus() == UserVerificationStatus.EXPIRED ||
            Duration.between(user.getCreatedTime(), LocalDateTime.now()).toMinutes() > EXPIRED_INTERVAL_IN_MINUTE;
    }


    private void sendVerificationEmail(RganUser user){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getVerificationEmail());
        simpleMailMessage.setSubject("Rgan Email Verification");

        String verificationLink = ServletUriComponentsBuilder.fromCurrentContextPath()
            .replacePath("/verification").path("/email")
            .queryParam("token", user.getVerificationToken()).build().toUriString();

        simpleMailMessage.setText(verificationLink);

        javaMailSender.send(simpleMailMessage);
    }
}
