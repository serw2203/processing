package org.example.app.processor;

import lombok.AllArgsConstructor;
import org.example.model.Operation;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OperationProcessorImpl implements OperationProcessor<Operation>{
    @Override
    public void apply(Operation operation) {
        System.out.println("OperationProcessorImpl.apply ====== >>>");
    }

    @Override
    public void revert(Operation operation) {
        System.out.println("OperationProcessorImpl.revert ====== >>>");
    }
}
