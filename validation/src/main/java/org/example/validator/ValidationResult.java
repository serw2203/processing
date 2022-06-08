package org.example.validator;

import lombok.Getter;
import org.example.infrastructure.generator.classfields.EnumClassField;
import org.example.infrastructure.generator.classfields.InstanceClassField;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class ValidationResult<T> {

    private List<ValidationMessage> errors = new ArrayList<>();
    private ValidationContext<T> context;
    private Map<ValidationContext<?>, ValidationResult<?>> enclosedValidationResult = new LinkedHashMap<>();

    public ValidationResult(ValidationContext context) {
        this.context = context;
    }

    public ValidationResult(T validateObject) {
        context = new ValidationContext(validateObject);
    }

    public boolean isPassed() {
        return getAllErrors().isEmpty();
    }

    public void addErrors(ValidationMessage... err) {
        Collections.addAll(errors, err);
    }

    public void addErrors(ValidationMessage.ValidationMessageBuilder... errorMessageBuilders) {
        Stream.of(errorMessageBuilders)
                .map(ValidationMessage.ValidationMessageBuilder::build)
                .forEach(this::addErrors);
    }

    public List<ValidationMessage> getAllErrors() {
        List<ValidationMessage> messages = enclosedValidationResult.values().stream()
                .map(ValidationResult::getAllErrors)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        messages.addAll(errors);
        return messages;
    }

    public void addResult(ValidationResult vr) {
        enclosedValidationResult.put(vr.getContext(), vr);
    }

    public Object getValue(InstanceClassField<T> field) {
        return field.getClassField().getValueExtractor().apply(context.getValidateObject());
    }

    public Object getValue(EnumClassField<T> field) {
        return field.getValueExtractor().apply(context.getValidateObject());
    }

    public <N> ValidationContext<N> createNestedContext(EnumClassField<T> classField) {
        return context.createNestedContext(classField);
    }

    public <N> ValidationContext<N> createNestedContextForCollection(EnumClassField<N> classField, N element, Integer index) {
        return context.createNestedContextForCollection(classField, element, index);
    }

    public <N> ValidationResult<N> getOrCreateEnclosedResult(ValidationContext<N> context, ValidationResult<N> validationResult) {
        return (ValidationResult<N>) enclosedValidationResult.getOrDefault(context, validationResult);
    }

    public <N> ValidationResult<N> getOrCreateEnclosedResult(ValidationContext<N> context) {
        enclosedValidationResult.putIfAbsent(context, new ValidationResult(context));
        return (ValidationResult<N>)  enclosedValidationResult.get(context);
    }

    public ValidationResult<Object> getOrCreateEnclosedResult(EnumClassField<T> enumClassField) {
        return getOrCreateEnclosedResult(createNestedContext(enumClassField));
    }

    public InstanceClassField<T> createNestedField(EnumClassField<T> enumClassField) {
        return createNestedField(enumClassField, null);
    }

    public InstanceClassField<T> createNestedField(EnumClassField<T> enumClassField, Integer index) {
        if (getContext() != null) {
            return getContext().createNestedField(enumClassField, index);
        }
        return InstanceClassField.createInstanceField(enumClassField, index);
    }
}
