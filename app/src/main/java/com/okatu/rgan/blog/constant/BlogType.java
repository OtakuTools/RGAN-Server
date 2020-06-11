package com.okatu.rgan.blog.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.okatu.rgan.common.model.RganAbstractPersistableEnumConverter;
import com.okatu.rgan.common.model.RganPersistableEnum;

import java.util.Arrays;

public enum BlogType implements RganPersistableEnum {
//    @JsonProperty("0")
    ORIGINAL(0),

//    @JsonProperty("1")
    REPRODUCTION(1),

//    @JsonProperty("2")
    TRANSLATION(2);

    private final int value;

    BlogType(int value) {
        this.value = value;
    }


    @JsonValue
    @Override
    public int getValue() {
        return value;
    }

    public static class Converter extends RganAbstractPersistableEnumConverter<BlogType>{
        public Converter() {
            super(BlogType.class);
        }
    }

    @JsonCreator
    public static BlogType selectByValue(int value){
        return Arrays.stream(values()).filter(status -> status.getValue() == value).findAny()
            .orElseThrow(() -> new IllegalArgumentException("No such value: " + value + " for enum class BlogType"));
    }

}
