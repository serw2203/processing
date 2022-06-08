package org.example.validator;


import org.example.infrastructure.TypeSpecificService;
import org.example.validator.annotation.AnnotationValidator;

public abstract class Validator<T> implements TypeSpecificService<T> {

    protected AnnotationValidator annotationValidator = new AnnotationValidator();

    public ValidationResult validate(T validatedObject) {
        return validate(validatedObject, new ValidationResult(validatedObject));
    }

    public ValidationResult validate(T validatedObject, ValidationResult<T> result) {
        annotationValidator.validateStringMaxLength(result);
        annotationValidator.validateNotNull(result);
        doValidate(validatedObject, result);
        return result;
    }

    protected abstract ValidationResult doValidate(T validatedObject, ValidationResult<T> result);
}
