package org.example.app.service.calculator;

public class CalculateActionAdd implements CalculateAction {

    public static final String ACTION_NAME =  " + ";

    @Override
    public Long perform(Long v1, Long v2) {
        return v1 + v2;
    }

    @Override
    public String name() {
        return ACTION_NAME;
    }
}
