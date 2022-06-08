package org.example.validator;

import lombok.AllArgsConstructor;
import org.example.infrastructure.TypeSpecificServiceResolver;
import org.example.infrastructure.generator.classfields.EnumClassField;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ValidationService {

    private TypeSpecificServiceResolver resolver;

    @SuppressWarnings("unchecked")
    public <T> ValidationResult<T> validateSafely(T o) {
        Optional<Validator> validator = findValidatorSafely(o);
        if (o != null && validator.isPresent()) {
            return validator.get().validate(o);
        }
        return new ValidationResult(o);
    }

    @SuppressWarnings("unchecked")
    public <T> ValidationResult<T> validate(T o, ValidationResult<T> vr) {
        Validator validator = findValidator(o);
        return validator.validate(o, vr);
    }

    @SuppressWarnings("unchecked")
    public <T> ValidationResult<T> validate(T o) {
        ValidationResult vr = new ValidationResult(o);
        Validator validator = findValidator(o);
        return validator.validate(o, vr);
    }

    @SuppressWarnings("unchecked")
    public void validateInContext(ValidationResult result, EnumClassField classField) {
        ValidationContext context = result.createNestedContext(classField);
        if (context.getValidateObject() == null) {
            return;
        }
        Validator validator = findValidator(context.getValidateObject());
        result.addResult(
            validator.validate(context.getValidateObject(),
                result.getOrCreateEnclosedResult(context, new ValidationResult(context))
            )
        );
    }

    @SuppressWarnings("unchecked")
    public void validateInContext(ValidationResult result, ValidationContext context) {
        if (context.getValidateObject() == null) {
            return;
        }
        Validator validator = findValidator(context.getValidateObject());
        result.addResult(
            validator.validate(context.getValidateObject(),
                result.getOrCreateEnclosedResult(context, new ValidationResult(context))
            )
        );
    }

    @SuppressWarnings("unchecked")
    public void validateCollection(Collection collection, EnumClassField enumClassField, ValidationResult result) {
        if (collection != null) {
            Iterator iterator = collection.iterator();
            for (int index = 0; iterator.hasNext(); index++) {
                validateInContext(result,
                    result.createNestedContextForCollection(enumClassField, iterator.next(), index));
            }
        }
    }

    private Validator findValidator(Object o) {
        return resolver.resolveStrictly(o.getClass(), Validator.class);
    }

    private Optional<Validator> findValidatorSafely(Object o) {
        return resolver.resolve(o.getClass(), Validator.class);
    }
}
