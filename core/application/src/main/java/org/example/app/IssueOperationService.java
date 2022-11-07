package org.example.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.app.processor.ProcessorsMappingService;
import org.example.model.Operation;
import org.example.validator.ValidationException;
import org.example.validator.ValidationResult;
import org.example.validator.ValidationService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class IssueOperationService<O extends Operation> {

    private final ValidationService validationService;
    private final ProcessorsMappingService processorsMappingService;

    private void applyActivate(O operation) {
        processorsMappingService.getProcessors(operation.getClass(), operation.getStatus()).forEach(prc -> prc.apply(operation));
    }

    /**
     * метод активации операции, применяет операцию к сделке
     */
    protected void activate(O operation) {
        operation.setActive(true);
        applyActivate(operation);
    }

    /**
     * метод деактивации операции, применяет операцию к сделке
     */
    protected void deactivate(O operation) {
        operation.setActive(false);
        applyDeactivate(operation);
    }

    private void applyDeactivate(O operation) {
        processorsMappingService.getProcessors(operation.getClass(), operation.getStatus()).forEach(prc -> prc.revert(operation));
    }

    private List<ValidationResult<?>> validateAndSave(O operation) {
        List<ValidationResult<?>> validationResults = validate(operation);
        return validationResults;
    }

    private List<ValidationResult<?>> validate(O operation) {
        ValidationResult<?> result = validationService.validate(operation);
        if (!result.isPassed()) {
            throw new ValidationException(Collections.singletonList(result));
        }
        return Collections.singletonList(result);
    }

    protected void apply(O operation) {
        if (operation.getStatus().isActivating()) {
            activate(operation);
        } else if (operation.getStatus().isDeactivating()) {
            deactivate(operation);
        }
    }

    public Result<O> create(O operation) {
        List<ValidationResult<?>> validationResults = validateAndSave(operation);

        apply(operation);

        return new Result(operation, validationResults);
    }

    /**
     * Результат сохранения операции
     */
    @Getter
    public static class Result<T extends Operation> {

        private T value;
        private List<ValidationResult<?>> validationResult;

        Result(T operation, List<ValidationResult<?>> validationResult) {
            this.value = operation;
            this.validationResult = validationResult;
        }
    }
}
