package org.example.infrastructure.generator.classfields.name;

public interface FieldNameGenerator<T> {
    String generateFor(T value);
}

