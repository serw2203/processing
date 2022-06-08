package org.example.app.converters;

import java.util.function.Function;

/**
 * Приводит value к представлению, соответствующему типу destinationType
 */
public interface TypeRepresentationConverter {

    <T> T convert(Object value, Class<T> destinationType);

    default <FROM, TO> Function<FROM, TO> convert(Class<TO> destinationType) {
        return value -> convert(value, destinationType);
    }
}
