package org.example.validator.flow;


public interface Statusable {
    Status getStatus();

    FlowRules flowRules();
}
