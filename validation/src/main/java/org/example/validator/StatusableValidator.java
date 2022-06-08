package org.example.validator;

import org.example.infrastructure.generator.classfields.EnumClassField;
import org.example.infrastructure.generator.classfields.InstanceClassField;
import org.example.validator.flow.FlowRules;
import org.example.validator.flow.Status;
import org.example.validator.flow.Statusable;

import static java.util.Optional.ofNullable;
import static org.example.validator.BaseValidationCode.STATUS_FLOW_ERROR;

public abstract class StatusableValidator<T extends Statusable> extends Validator<T>  {

    protected static final String EMPTY_STATUS_NAME = "'не установлен'";

    protected abstract EnumClassField getStatusField();

    protected abstract Status getCurrentStatus(T currentObject);

    @Override
    protected ValidationResult doValidate(T validatedObject, ValidationResult<T> result) {
        validateFlow(validatedObject, result);

        annotationValidator.validateStatusableNotNull(result);
        return result;
    }

    private void validateFlow(T validatedObject, ValidationResult result) {
        Status currentStatus = getCurrentStatus(validatedObject);
        Status newStatus = validatedObject.getStatus();

        if (currentStatus != null) {
            if (!mayBeMovedToStatus(validatedObject, currentStatus)) {
                addError(currentStatus, newStatus, result);
            }
        } else if (!getFlowRules(validatedObject).isAllowedInitialState(newStatus)) {
            addError(currentStatus, newStatus, result);
        }
    }

    private void addError(Status currentStatus, Status newStatus, ValidationResult result) {
        InstanceClassField field = result.createNestedField(getStatusField());
        result.addErrors(STATUS_FLOW_ERROR.message()
                .field(field)
                .param(getStatusNameFor(currentStatus))
                .param(getStatusNameFor(newStatus))
                .build()
        );
    }

    private String getStatusNameFor(Status status) {
        return ofNullable(status).map(Object::toString).orElse(EMPTY_STATUS_NAME);
    }

    private FlowRules getFlowRules(T validatableObject) {
        return validatableObject.flowRules();

    }

    private boolean mayBeMovedToStatus(T validatableObject, Status currentStatus) {
        return getFlowRules(validatableObject).isAllowedTransition(currentStatus, validatableObject.getStatus());
    }

}
