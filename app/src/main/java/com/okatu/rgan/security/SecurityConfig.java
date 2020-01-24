package com.okatu.rgan.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll()
                .and().csrf().disable();
    }

    @Bean
    public DefaultCookieSerializer cookieSerializer(){
        return new DefaultCookieSerializer();
    }

    private class CookieSerializerConfigure implements ServletContainerInitializer{
        @Override
        public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
            SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
            // about the setting, see:
            // https://docs.spring.io/spring-session/docs/current/api/org/springframework/session/web/http/DefaultCookieSerializer.html
            // https://github.com/spring-projects/spring-session/issues/87
            DefaultCookieSerializer cookieSerializer = cookieSerializer();
            cookieSerializer.setUseSecureCookie(sessionCookieConfig.isSecure());
            cookieSerializer.setUseHttpOnlyCookie(sessionCookieConfig.isHttpOnly());
            cookieSerializer.setCookiePath(sessionCookieConfig.getPath());
            cookieSerializer.setCookieName(sessionCookieConfig.getName());
            cookieSerializer.setCookieMaxAge(sessionCookieConfig.getMaxAge());
            cookieSerializer.setDomainName(sessionCookieConfig.getDomain());
        }
    }
}