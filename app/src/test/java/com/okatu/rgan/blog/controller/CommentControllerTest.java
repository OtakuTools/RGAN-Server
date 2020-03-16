package com.okatu.rgan.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okatu.rgan.blog.model.param.CommentEditParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void all() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/blogs/1/comments");

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }


    @Test
    @WithUserDetails("test2")
    void add() throws Exception {
        CommentEditParam commentEditParam = new CommentEditParam();
        commentEditParam.setContent("abc");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/blogs/1/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentEditParam));
        mockMvc.perform(requestBuilder)
            .andDo(print());
    }
}