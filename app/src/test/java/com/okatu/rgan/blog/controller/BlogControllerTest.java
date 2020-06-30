package com.okatu.rgan.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okatu.rgan.blog.constant.BlogStatus;
import com.okatu.rgan.blog.constant.BlogType;
import com.okatu.rgan.blog.model.param.BlogEditParam;
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
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGet() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/blogs/7");

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    @Test
    @WithUserDetails("test1")
    public void testBlogPost() throws Exception{
        BlogEditParam blogEditParam = new BlogEditParam();
        blogEditParam.setTitle("post 12");
        blogEditParam.setContent("##2");
        blogEditParam.setStatus(BlogStatus.PUBLISHED);
        blogEditParam.setType(BlogType.ORIGINAL);
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

    @Test
    @WithUserDetails("test1")
    void testBlogPut() throws Exception{
        BlogEditParam blogEditParam = new BlogEditParam();
        blogEditParam.setTitle("change");
        blogEditParam.setContent("fuack me waht");
        blogEditParam.setStatus(BlogStatus.PUBLISHED);
        blogEditParam.setType(BlogType.ORIGINAL);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/blogs/12")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(blogEditParam));

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    @Test
    void testSearch() throws Exception{

        String keywords = "this user";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/blogs/search?keyword=" + keywords);

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    @Test
    void search() throws Exception{
        String keywords = "Test";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/blogs/search?keyword=" + keywords);

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    @WithUserDetails("test1")
    @Test
    void addBlogToUserFavouriteList() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/blogs/42/favourite");

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }
}