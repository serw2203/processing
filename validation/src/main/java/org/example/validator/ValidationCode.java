package org.example.validator;

public interface ValidationCode {

    String getMessageTemplate();

    default ValidationMessage.ValidationMessageBuilder message() {
        return ValidationMessage.builder().code(this);
    }
}
