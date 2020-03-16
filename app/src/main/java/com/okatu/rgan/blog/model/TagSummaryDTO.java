package com.okatu.rgan.blog.model;

import com.okatu.rgan.blog.model.projection.TagSummaryProjection;

public class TagSummaryDTO {
    private Long id;

    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TagSummaryDTO() {
    }

    public TagSummaryDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static TagSummaryDTO convertFrom(TagSummaryProjection projection){
        return new TagSummaryDTO(projection.getId(), projection.getTitle());
    }
}
