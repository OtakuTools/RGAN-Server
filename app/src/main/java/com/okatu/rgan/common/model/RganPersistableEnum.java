package com.okatu.rgan.common.model;

// see https://fasterxml.github.io/jackson-annotations/javadoc/2.8/com/fasterxml/jackson/annotation/JsonValue.html
// when use for Java enums ...
// JsonProperty("1") cannot only be used with String
public interface RganPersistableEnum {
    int getValue();
}
