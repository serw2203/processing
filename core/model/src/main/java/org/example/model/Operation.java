package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.infrastructure.generator.classfields.GenerateClassField;
import org.example.validator.flow.FlowRules;
import org.example.validator.flow.Statusable;

@GenerateClassField
@Getter
@Setter
@NoArgsConstructor
public class Operation implements Statusable {

    private OperationStatus status;
    private Operation parentOperation;
    private Boolean active;

    public FlowRules flowRules() {
        return this.getClass().getAnnotation(OperationType.class).flow().getRule();
    }

}
