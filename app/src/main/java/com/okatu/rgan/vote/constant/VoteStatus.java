package com.okatu.rgan.vote.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.okatu.rgan.common.model.RganAbstractPersistableEnumConverter;
import com.okatu.rgan.common.model.RganPersistableEnum;

import java.util.Arrays;

public enum VoteStatus implements RganPersistableEnum {
//    @JsonProperty("1")
    UPVOTE(1),

//    @JsonProperty("0")
    CANCELED(0),

//    @JsonProperty("-1")
    DOWNVOTE(-1);

    private final int value;

    VoteStatus(int value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public int getValue() {
        return value;
    }

    public static class Converter extends RganAbstractPersistableEnumConverter<VoteStatus> {
        public Converter() {
            super(VoteStatus.class);
        }
    }


    @JsonCreator
    public static VoteStatus selectByValue(int value){
        return Arrays.stream(values()).filter(status -> status.getValue() == value).findAny()
            .orElseThrow(() -> new IllegalArgumentException("No such value: " + value + " for enum class VoteStatus"));
    }
}
