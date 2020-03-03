package com.okatu.rgan.user.authentication.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class RganRememberMeServices extends PersistentTokenBasedRememberMeServices {
    public RganRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        return Objects.equals(request.getAttribute(DEFAULT_PARAMETER), true);
    }
}
