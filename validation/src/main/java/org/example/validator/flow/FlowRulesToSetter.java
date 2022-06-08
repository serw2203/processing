package org.example.validator.flow;


public interface FlowRulesToSetter<T extends Status> {
    FlowRulesFromSetter<T> toState(T... toStates);
}
