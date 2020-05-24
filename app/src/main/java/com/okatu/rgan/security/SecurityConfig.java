package com.okatu.rgan.security;

import com.okatu.rgan.user.authentication.handler.success.RganLogoutSuccessHandler;
import com.okatu.rgan.user.authentication.service.RganRememberMeServices;
import com.okatu.rgan.user.authentication.service.RganUserDetailService;
import com.okatu.rgan.user.authentication.handler.AcceptJsonUsernamePasswordAuthenticationFilter;
import com.okatu.rgan.user.authentication.handler.RganAuthenticationEntryPoint;
import com.okatu.rgan.user.authentication.handler.success.RganAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

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
            .tokenValiditySeconds(60 * 60 * 24)
            .and()
            .authorizeRequests()
            .antMatchers(
                "/register",
                "/login",
                "/logout"
            ).permitAll()
            .antMatchers(
                HttpMethod.GET,
                "/blogs",
                "/tags",
                "/users/{id:\\d+}/**",
                "/blogs/{id:\\d+}/**",
                "/tags/{id:\\d+}/**",
                "/blogs/search"
            ).permitAll()
            .antMatchers(
                HttpMethod.GET,
                "/blogs/vote/status",
                "/comments/vote/status",
                "/users/self/blogs"
            ).authenticated()
            .antMatchers(
                HttpMethod.POST,
                "/verification/email/receive"
            ).permitAll()
            .anyRequest().authenticated()
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
        acceptJsonUsernamePasswordAuthenticationFilter.setRememberMeServices(rememberMeServices());
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
        return new RganRememberMeServices("guess-what?", userDetailsService(), persistentTokenRepository());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

}