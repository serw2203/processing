package org.example.infrastructure.generator.classfields;

import java.util.function.Function;
import java.util.stream.Stream;

public interface EnumClassField<T> {
    String getFieldName();

    Function<? super T, ?> getValueExtractor();

    String toString();

    Class<T> getObjectType();

    static EnumClassField valueOfFieldName(String fieldName, Class<? extends EnumClassField> enumClassFieldClass) {
        return Stream.of(enumClassFieldClass.getEnumConstants())
            .filter(it -> it.getFieldName().equals(fieldName))
            .findFirst()
            .get();
    }
}
