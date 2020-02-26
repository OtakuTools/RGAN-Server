package com.okatu.rgan.user.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okatu.rgan.user.model.param.SendVerificationEmailParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class VerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("test1")
    void sendVerificationEmail() throws Exception{
        SendVerificationEmailParam sendVerificationEmailParam = new SendVerificationEmailParam();
        sendVerificationEmailParam.setEmail("loveshady1@163.com");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/verification/email/send")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sendVerificationEmailParam));

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    @Test
    void receiveVerificationToken() {
    }
}