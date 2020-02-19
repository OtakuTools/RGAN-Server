package com.okatu.rgan.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okatu.rgan.blog.model.BlogEditParam;
import com.okatu.rgan.blog.model.TagEditParam;
import com.okatu.rgan.blog.model.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGet() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/blogs");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void testTagPost() throws Exception{
        TagEditParam tagEditParam = new TagEditParam();
        tagEditParam.setTitle("");
        tagEditParam.setDescription("描述..");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagEditParam));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void testBlogPost() throws Exception{
        BlogEditParam blogEditParam = new BlogEditParam();
        blogEditParam.setTitle("");
        blogEditParam.setContent("##");
//        Set<String> tags = new HashSet<>();
//        tags.add("测试1");
//        blogEditParam.setTags(tags);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/blogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blogEditParam));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}