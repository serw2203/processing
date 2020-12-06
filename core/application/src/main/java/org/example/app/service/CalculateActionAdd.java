package org.example.app.service;

public class CalculateActionAdd implements CalculateAction {
    @Override
    public Long perform(Long v1, Long v2) {
        return v1 + v2;
    }

    @Override
    public String name() {
        return " + ";
    }
}
