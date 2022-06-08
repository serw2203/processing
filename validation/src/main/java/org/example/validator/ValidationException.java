package org.example.validator;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException {
    List<ValidationResult<?>> validationResults;
}
