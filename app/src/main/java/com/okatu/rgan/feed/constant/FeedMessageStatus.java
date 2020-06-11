package com.okatu.rgan.feed.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.okatu.rgan.common.model.RganAbstractPersistableEnumConverter;
import com.okatu.rgan.common.model.RganPersistableEnum;

import java.util.Arrays;

public enum FeedMessageStatus implements RganPersistableEnum {

//    @JsonProperty("0")
    ENABLED(0),

//    @JsonProperty("1")
    DELETED(1);

    private final int value;


    FeedMessageStatus(int value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static FeedMessageStatus selectByValue(int value){
        return Arrays.stream(values()).filter(status -> status.getValue() == value).findAny()
            .orElseThrow(() -> new IllegalArgumentException("No such value: " + value + " for enum class FeedMessageStatus"));
    }

    public static class Converter extends RganAbstractPersistableEnumConverter<FeedMessageStatus> {
        public Converter() {
            super(FeedMessageStatus.class);
        }
    }
}
