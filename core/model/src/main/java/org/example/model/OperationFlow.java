package org.example.model;

import lombok.Getter;
import org.example.validator.flow.Flow;
import org.example.validator.flow.FlowRules;
import org.example.validator.flow.FlowRulesBuilder;

import static org.example.model.OperationStatus.*;

@Getter
public enum OperationFlow implements Flow {

    STANDARD(FlowRulesBuilder.<OperationStatus>create()
        .initial(DRAFT)
        .fromState(DRAFT).toState(IN_REVIEW, APPROVED)
        .fromState(IN_REVIEW).toState(APPROVED, REJECTED)
        .fromState(REJECTED).toState(IN_REVIEW, INACTIVE)
        .fromState(APPROVED).toState(INACTIVE)
        .build()),
    SHORT(FlowRulesBuilder.<OperationStatus>create()
        .initial(APPROVED)
        .fromState(APPROVED).toState(INACTIVE)
        .build());

    OperationFlow(FlowRules<OperationStatus> rule) {
        this.rule = rule;
    }

    private FlowRules<OperationStatus> rule;
}
