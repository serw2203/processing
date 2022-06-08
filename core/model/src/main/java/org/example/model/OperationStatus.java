package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.validator.flow.Status;

@Getter
@AllArgsConstructor
public enum OperationStatus implements Status {
    DRAFT(),
    IN_REVIEW(),
    APPROVED(true, false),
    REJECTED(),
    INACTIVE(false, true);

    OperationStatus() {
    }

    boolean activating;
    boolean deactivating;
}
