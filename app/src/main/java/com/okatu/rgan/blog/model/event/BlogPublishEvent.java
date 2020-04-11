package com.okatu.rgan.blog.model.event;

import com.okatu.rgan.blog.model.entity.Blog;
import org.springframework.context.ApplicationEvent;

public class BlogPublishEvent extends ApplicationEvent {
    private final Blog blog;

    public BlogPublishEvent(Object source, Blog blog) {
        super(source);
        this.blog = blog;
    }

    public Blog getBlog() {
        return blog;
    }
}
