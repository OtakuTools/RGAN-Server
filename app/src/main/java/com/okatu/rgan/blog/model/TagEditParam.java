package com.okatu.rgan.blog.model;

import javax.validation.constraints.NotBlank;

public class TagEditParam {

    @NotBlank
    private String title;

    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TagEditParam() {
    }

    @Override
    public String toString() {
        return "TagEditParam{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
