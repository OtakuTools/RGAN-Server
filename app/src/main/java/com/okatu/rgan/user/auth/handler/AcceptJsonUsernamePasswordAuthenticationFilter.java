package com.okatu.rgan.user.auth.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

// about how remember-me is handled, see:
// https://blog.csdn.net/jiujiuming/article/details/68488504
public class AcceptJsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static String REMEMBER_ME_PARAMETER_NAME = "rememberMe";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = "";
            String password = "";

            try(InputStream inputStream = request.getInputStream()){
                Map<String, String> loginParam = objectMapper.readValue(
                    request.getInputStream(),
                    new TypeReference<Map<String, String>>() {}
                );

                username = loginParam.getOrDefault(this.getUsernameParameter(), "");
                password = loginParam.getOrDefault(this.getPasswordParameter(), "");

                String rememberMeNeed = loginParam.get(REMEMBER_ME_PARAMETER_NAME);
                if(rememberMeNeed != null && (rememberMeNeed.equalsIgnoreCase("true") ||
                    rememberMeNeed.equalsIgnoreCase("on") ||
                    rememberMeNeed.equalsIgnoreCase("yes") ||
                    rememberMeNeed.equals("1"))){
                    request.setAttribute(AbstractRememberMeServices.DEFAULT_PARAMETER, true);
                }

            }catch (IOException e){
                logger.error("Error reading username and password while login", e);
                // TODO: check how spring handle this exception and fill the error message
                throw new AuthenticationServiceException("Can not get authentication from request");
            }

            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }
}
