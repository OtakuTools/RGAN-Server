package com.okatu.rgan.blog.model.param;

import com.okatu.rgan.blog.constant.BlogStatus;
import com.okatu.rgan.blog.constant.BlogType;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;

public class BlogEditParam {

    private String title;

    @NotNull
    private BlogType type;

    @NotNull
    private BlogStatus status;

    private String summary;

    private String content;

    // must guarantee no duplicated and sorted by insertion order
    private LinkedHashSet<String> tags = new LinkedHashSet<>();

    public BlogStatus getStatus() {
        return status;
    }

    public void setStatus(BlogStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BlogType getType() { return type; }

    public void setType(BlogType type) { this.type = type; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LinkedHashSet<String> getTags() {
        return tags;
    }

    public void setTags(LinkedHashSet<String> tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public BlogEditParam() {
    }

}
