package org.example.app.operation.registration.event;

import lombok.extern.slf4j.Slf4j;
import org.example.logger.LogIt;
import org.example.model.Agreement;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefineTariffRegistrationAgreement {

    @LogIt
    public Agreement define(Agreement agreement) {
        log.info("Define tariff code {} on agreement № {}", "605", agreement.getNumber());
        return agreement.withTariffCode("605");
    }
}
