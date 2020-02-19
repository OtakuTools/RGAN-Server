package com.okatu.rgan.user.auth.handler.success;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RganAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // don't need to flush/close the outputstream, see:
    // https://stackoverflow.com/questions/5043657/do-i-need-to-flush-the-servlet-outputstream
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.getWriter().write("Authentication success");
    }
}
