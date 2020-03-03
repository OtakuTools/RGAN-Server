package com.okatu.rgan.user.authentication.config;

import com.okatu.rgan.user.authentication.handler.failure.AuthenticationServiceExceptionHandler;
import com.okatu.rgan.user.authentication.handler.failure.BadCredentialsExceptionHandler;
import com.okatu.rgan.user.authentication.handler.failure.UsernameNotFoundExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationFailureHandler;

import java.util.LinkedHashMap;

@Configuration
public class AuthenticationExceptionHandlerConfig {
    @Autowired
    private AuthenticationServiceExceptionHandler authenticationServiceExceptionHandler;

    @Autowired
    private BadCredentialsExceptionHandler badCredentialsExceptionHandler;

    @Autowired
    private UsernameNotFoundExceptionHandler usernameNotFoundExceptionHandler;

    @Bean
    public DelegatingAuthenticationFailureHandler authenticationFailureHandler(){
        LinkedHashMap<Class<? extends AuthenticationException>, AuthenticationFailureHandler> handlers = new LinkedHashMap<>();
        handlers.put(AuthenticationServiceException.class, authenticationServiceExceptionHandler);
        handlers.put(BadCredentialsException.class, badCredentialsExceptionHandler);
        handlers.put(UsernameNotFoundException.class, usernameNotFoundExceptionHandler);
        return new DelegatingAuthenticationFailureHandler(handlers, authenticationServiceExceptionHandler);
    }
}
