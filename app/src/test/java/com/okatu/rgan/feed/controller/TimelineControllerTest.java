package com.okatu.rgan.feed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okatu.rgan.feed.model.param.TimelineMessageReadStatusUpdateParam;
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

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class TimelineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("test1")
    void getReplyComment() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/timeline/comments");

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    @Test
    @WithUserDetails("test")
    void updateTimelineMessageReadStatus() throws Exception {
        TimelineMessageReadStatusUpdateParam param = new TimelineMessageReadStatusUpdateParam();
        Set<Long> messageIds = new HashSet<>();
        messageIds.add(20L);
        param.setMessageItemIds(messageIds);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/timeline/read")
            .content(objectMapper.writeValueAsString(param))
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

}