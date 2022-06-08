package org.example.validator;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.example.infrastructure.generator.classfields.InstanceClassField;

import java.util.List;

@Data
@Builder
public class ValidationMessage {

    private final ValidationCode code;
    @Singular
    private List<Object> params;
    private InstanceClassField field;

    public String getMessage() {
        return String.format(
            code.getMessageTemplate(),
            params.toArray());
    }
}
