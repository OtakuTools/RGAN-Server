package com.okatu.rgan.feed.constant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.okatu.rgan.common.model.RganAbstractPersistableEnumConverter;
import com.okatu.rgan.common.model.RganPersistableEnum;

import java.util.Arrays;

public enum FeedMessageType implements RganPersistableEnum {

    @JsonProperty("0")
    BLOG(0),

    @JsonProperty("1")
    COMMENT(1),

    @JsonProperty("2")
    BLOG_VOTE(2),

    @JsonProperty("3")
    COMMENT_VOTE(3);

    private final int value;

    FeedMessageType(int value){
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static FeedMessageType selectByValue(int value){
        return Arrays.stream(values()).filter(status -> status.getValue() == value).findAny()
            .orElseThrow(() -> new IllegalArgumentException("No such value: " + value + " for enum class FeedMessageType"));
    }

    public static class Converter extends RganAbstractPersistableEnumConverter<FeedMessageType> {
        public Converter() {
            super(FeedMessageType.class);
        }
    }
}
