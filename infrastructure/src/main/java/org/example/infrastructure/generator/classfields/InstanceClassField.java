package org.example.infrastructure.generator.classfields;

import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@Getter
public class InstanceClassField<T> {
    private final EnumClassField<T> classField;
    private final InstanceClassField parentField;
    private final Integer index;

    public static <T> InstanceClassField<T> createInstanceField(EnumClassField<T> enumClassField) {
        return new InstanceClassField<>(enumClassField, null);
    }

    public static <T> InstanceClassField<T> createInstanceField(EnumClassField<T> enumClassField, Integer index) {
        return new InstanceClassField<>(enumClassField, index);
    }

    public <N> InstanceClassField<N> createNestedField(EnumClassField<N> classField) {
        return new InstanceClassField<>(classField, this, null);
    }

    public <N> InstanceClassField<N> createNestedField(EnumClassField<N> classField, Integer index) {
        return new InstanceClassField<>(classField, this, index);
    }

    private InstanceClassField(@NonNull EnumClassField<T> classField, Integer index) {
        this.classField = classField;
        this.parentField = null;
        this.index = index;
    }

    private InstanceClassField(@NonNull EnumClassField<T> classField,
                               @NonNull InstanceClassField parentField,
                               Integer index) {
        this.classField = classField;
        this.parentField = parentField;
        this.index = index;
    }

    public String name() {
        return classField.toString();
    }

    public String getFieldName() {
        return getClassField().getFieldName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceClassField that = (InstanceClassField) o;
        return Objects.equals(classField, that.classField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classField);
    }

    @Override
    public String toString() {
        return classField.getFieldName();
    }
}
