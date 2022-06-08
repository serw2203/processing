package org.example.validator;

import lombok.Getter;
import org.example.infrastructure.generator.classfields.EnumClassField;
import org.example.infrastructure.generator.classfields.InstanceClassField;

import java.util.Objects;

@Getter
public class ValidationContext<T> {

    private T validateObject;
    private InstanceClassField<T> field;
    private Integer index;

    public ValidationContext(T validateObject, InstanceClassField field) {
        this.validateObject = validateObject;
        this.field = field;
    }

    public ValidationContext(T validateObject, InstanceClassField field, Integer index) {
        this.validateObject = validateObject;
        this.field = field;
        this.index = index;
    }

    public ValidationContext(T validateObject) {
        this.validateObject = validateObject;
    }

    public <N> ValidationContext<N> createNestedContext(EnumClassField<T> classField) {
        return new ValidationContext<>((N) classField.getValueExtractor().apply(validateObject),
                field != null ? field.createNestedField(classField) : InstanceClassField.createInstanceField(classField));
    }

    public InstanceClassField createNestedField(EnumClassField<T> enumClassField, Integer index) {
        if (getField() != null) {
            return getField().createNestedField(enumClassField, index);
        }
        return InstanceClassField.createInstanceField(enumClassField, index);
    }

    public <E> ValidationContext<E> createNestedContextForCollection(EnumClassField<E> classField, E element, Integer index) {
        return new ValidationContext<>(element,
                field != null ? field.createNestedField(classField, index) : InstanceClassField.createInstanceField(classField, index), index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValidationContext that = (ValidationContext) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(validateObject, that.validateObject) &&
                Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, validateObject, index);
    }


}
