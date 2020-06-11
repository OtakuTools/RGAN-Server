package com.okatu.rgan.user.feature.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class UserCenterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getFollowers() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/2/followers");
        mockMvc.perform(requestBuilder)
            .andDo(print());
    }

    @WithUserDetails("test")
    @Test
    void getSelfBlogs() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/self/blogs?page=10&status=0");
        mockMvc.perform(requestBuilder)
            .andDo(print());
    }
}