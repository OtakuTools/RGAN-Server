package com.okatu.rgan.security;

import com.okatu.rgan.user.auth.handler.success.RganLogoutSuccessHandler;
import com.okatu.rgan.user.auth.service.RganUserDetailService;
import com.okatu.rgan.user.auth.config.AuthenticationExceptionHandlerConfig;
import com.okatu.rgan.user.auth.handler.AcceptJsonUsernamePasswordAuthenticationFilter;
import com.okatu.rgan.user.auth.handler.RganAuthenticationEntryPoint;
import com.okatu.rgan.user.auth.handler.success.RganAuthenticationSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.servlet.*;
import javax.sql.DataSource;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DelegatingAuthenticationFailureHandler delegatingAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService())
            .addFilterAt(acceptJsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
            .and()
            .formLogin().loginProcessingUrl("/login")
            .successHandler(authenticationSuccessHandler())
            .failureHandler(delegatingAuthenticationFailureHandler)
            .and()
            .logout().logoutUrl("/logout")
            .logoutSuccessHandler(logoutSuccessHandler())
            .and()
            .rememberMe()
            .rememberMeServices(rememberMeServices())
            .and()
            .authorizeRequests()
            .antMatchers(
    "/register",
                "/login",
                "/logout"
            ).permitAll()
            .anyRequest().permitAll()
            .and()
            .csrf().disable()
            ;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService())
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AcceptJsonUsernamePasswordAuthenticationFilter acceptJsonUsernamePasswordAuthenticationFilter() throws Exception {
        AcceptJsonUsernamePasswordAuthenticationFilter acceptJsonUsernamePasswordAuthenticationFilter = new AcceptJsonUsernamePasswordAuthenticationFilter();
        acceptJsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        acceptJsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        acceptJsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(delegatingAuthenticationFailureHandler);
        return acceptJsonUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new RganAuthenticationEntryPoint();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new RganUserDetailService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new RganAuthenticationSuccessHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new RganLogoutSuccessHandler();
    }

    @Bean
    public RememberMeServices rememberMeServices(){
        return new PersistentTokenBasedRememberMeServices("guess-what?", userDetailsService(), persistentTokenRepository());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
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
            cookieSerializer.setSameSite("Lax");
        }
    }
}