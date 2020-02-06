package com.okatu.rgan.blog.model;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

public class BlogEditParam {
    @NotEmpty
    private String title;

    private String content;

    private Set<String> tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public BlogEditParam() {
    }

    @Override
    public String toString() {
        return "BlogEditParam{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tags=" + tags +
                '}';
    }
}
