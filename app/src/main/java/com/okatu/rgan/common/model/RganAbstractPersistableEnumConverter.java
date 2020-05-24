package com.okatu.rgan.common.model;

import javax.persistence.AttributeConverter;
import java.util.stream.Stream;

public class RganAbstractPersistableEnumConverter<T extends Enum<T> & RganPersistableEnum> implements AttributeConverter<T, Integer> {
    private final Class<T> clazz;

    public RganAbstractPersistableEnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Integer convertToDatabaseColumn(T t) {
        return t == null ? null : t.getValue();
    }

    @Override
    public T convertToEntityAttribute(Integer columnValue) {
        if(columnValue == null){
            return null;
        }

        return Stream.of(clazz.getEnumConstants())
            .filter(rganPersistableEnum -> rganPersistableEnum.getValue() == columnValue).findAny()
            .orElseThrow(() -> new IllegalArgumentException("No such value: " + columnValue + " for enum class " + clazz.getName()));
    }
}
