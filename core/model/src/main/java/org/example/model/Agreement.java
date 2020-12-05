package org.example.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.io.Serializable;
import java.time.LocalDate;

@Builder(toBuilder = true)
@Getter
public class Agreement implements Serializable {

    private final String id;

    @Getter(AccessLevel.NONE)
    @Delegate(excludes = ExcludedMethod.class)
    private final AgreementState state;

    @Builder(toBuilder = true)
    @Getter
    public static class AgreementState implements Serializable {
        private String number;
        private LocalDate documentDate;
        private String attr1;
        private String attr2;
        private String tariffCode;
    }

    public Agreement withTariffCode(String tariffCode) {
        return this.toBuilder().state(
            this.state.toBuilder().
                tariffCode(tariffCode)
                .build()
        ).build();
    }
}
