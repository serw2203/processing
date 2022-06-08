package org.example.validator;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.example.infrastructure.generator.classfields.InstanceClassField;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class InstanceClassFields {

    @Singular
    private List<InstanceClassField> fields;
    private String separator;

    @Override
    public String toString() {
        return fields.stream().map(Object::toString).collect(Collectors.joining(separator));
    }
}
