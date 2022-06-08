package org.example.app.processor;

import org.example.model.Operation;
import org.example.model.OperationStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service /// todo : ???
public class ProcessorsMappingService {
    private final Map<Class<? extends Operation>, Map<OperationStatus, List<? extends OperationProcessor>>> mapping;

    public ProcessorsMappingService(Map<Class<? extends Operation>, Map<OperationStatus, List<? extends OperationProcessor>>> mapping) {
        this.mapping = mapping;
    }

    public List<OperationProcessor> getProcessors(Class<? extends Operation> operationClass, OperationStatus status) {
        return Collections.unmodifiableList(mapping.get(operationClass).getOrDefault(status, Collections.EMPTY_LIST));
    }
}
