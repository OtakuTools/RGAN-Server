package com.okatu.rgan.user.authentication.handler.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okatu.rgan.user.model.RganUser;
import com.okatu.rgan.user.model.RganUserDTO;
import com.okatu.rgan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class RganAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    // don't need to flush/close the outputstream, see:
    // https://stackoverflow.com/questions/5043657/do-i-need-to-flush-the-servlet-outputstream
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        RganUser user = (RganUser) authentication.getPrincipal();
        user.setLastLoginTime(LocalDateTime.now());

        httpServletResponse.getWriter().write(mapper.writeValueAsString(
            RganUserDTO.convertFrom(userRepository.save(user)))
        );
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.OK.value());

    }
}
