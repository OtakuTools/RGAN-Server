package com.okatu.rgan.blog.constant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.okatu.rgan.common.model.RganAbstractPersistableEnumConverter;
import com.okatu.rgan.common.model.RganPersistableEnum;

import java.util.Arrays;

public enum BlogStatus implements RganPersistableEnum {
    @JsonProperty("0")
    PUBLISHED(0),

    @JsonProperty("1")
    DRAFT(1),

    @JsonProperty("2")
    DELETED(2);

    private final int value;


    BlogStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static BlogStatus selectByValue(int value){
        return Arrays.stream(values()).filter(status -> status.getValue() == value).findAny()
            .orElseThrow(() -> new IllegalArgumentException("No such value: " + value + " for enum class BlogStatus"));
    }

    public static class Converter extends RganAbstractPersistableEnumConverter<BlogStatus> {
        public Converter() {
            super(BlogStatus.class);
        }
    }
}
