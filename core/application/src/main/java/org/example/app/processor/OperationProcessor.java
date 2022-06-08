package org.example.app.processor;

import org.example.model.Operation;

public interface OperationProcessor<O extends Operation> {
    void apply(O operation);
    void revert(O operation);
}
