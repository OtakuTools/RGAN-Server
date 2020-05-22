package com.okatu.rgan.user.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okatu.rgan.user.model.UserLoginPrincipal;
import com.okatu.rgan.user.authentication.model.param.UserRegisterParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class UserAuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void register() throws Exception {
        UserRegisterParam registerParam = new UserRegisterParam();

        registerParam.setUsername("test1");
        registerParam.setPassword("password length > 8");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(registerParam));

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    @Test
    void testLoginSuccess() throws Exception {
        UserLoginPrincipal loginPrincipal = new UserLoginPrincipal();

        loginPrincipal.setUsername("test1");
        loginPrincipal.setPassword("password length > 8");

        Map<String, String> params = new HashMap<>();

        params.put("username", "test1");
        params.put("password", "password length > 8");
        params.put("rememberMe", "true");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(params));

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    @Test
    void testLoginFailureCauseByUsernameDoNotExist() throws Exception{
        UserLoginPrincipal loginPrincipal = new UserLoginPrincipal();

        loginPrincipal.setUsername("no");
        loginPrincipal.setPassword("adadadada");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(loginPrincipal));

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()))
            .andDo(print());
    }

    @Test
    void testLoginFailureCauseByPasswordWrong() throws Exception{
        UserLoginPrincipal loginPrincipal = new UserLoginPrincipal();

        loginPrincipal.setUsername("test1");
        loginPrincipal.setPassword("adadadadad");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(loginPrincipal));

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()))
            .andDo(print());
    }
}