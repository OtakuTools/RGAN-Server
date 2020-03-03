package com.okatu.rgan.security;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import java.util.Set;

@Configuration
public class CookieConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
//    @Bean
//    public DefaultCookieSerializer cookieSerializer(){
//        return new DefaultCookieSerializer();
//    }
//
//    private class CookieSerializerConfigure implements ServletContextInitializer {
//        @Override
//        public void onStartup(ServletContext servletContext) throws ServletException {
//            SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
//            // about the setting, see:
//            // https://docs.spring.io/spring-session/docs/current/api/org/springframework/session/web/http/DefaultCookieSerializer.html
//            // https://github.com/spring-projects/spring-session/issues/87
//            DefaultCookieSerializer cookieSerializer = cookieSerializer();
//            cookieSerializer.setSameSite("Lax");
//        }
//    }
//
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
//        factory.addInitializers(new CookieSerializerConfigure());
    }
}
