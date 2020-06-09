package com.okatu.rgan.vote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okatu.rgan.vote.constant.VoteStatus;
import com.okatu.rgan.vote.model.param.VoteParam;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithUserDetails("test1")
    void voteBlog() throws Exception {
        VoteParam voteParam = new VoteParam();
        voteParam.setStatus(VoteStatus.DOWNVOTE);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/blogs/6/vote")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(voteParam));

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    @Test
    @WithUserDetails("test1")
    void selfBlogVoteStatus() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/blogs/vote/status?id[]=5&id[]=6");

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }
}