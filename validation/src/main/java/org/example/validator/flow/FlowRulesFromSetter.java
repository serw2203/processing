package org.example.validator.flow;


import org.example.infrastructure.Builder;

public interface FlowRulesFromSetter<T extends Status> extends Builder<FlowRules<T>> {
    FlowRulesFromSetter<T> initial(T... initialStates);

    FlowRulesToSetter<T> fromState(T from);

    FlowRulesFromSetter<T> fromEmptyToAllExistingStates();

    Builder<FlowRules<T>> terminal(T... terminalStates);
}
