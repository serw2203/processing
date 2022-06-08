package org.example.validator;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ValidationAspect {

    private final ValidationService validationService;

    @Autowired
    public ValidationAspect(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Before("@annotation(org.example.validator.ValidateMethodParams)")
    public void validate(JoinPoint jp) {
        List<ValidationResult<?>> results = Arrays.stream(jp.getArgs())
            .map(validationService::validateSafely)
            .collect(Collectors.toList());

        if (!results.stream().allMatch(ValidationResult::isPassed)) {
            log.warn("Validation errors occurred: " + results);
            throw new ValidationException(results);
        }
    }
}
