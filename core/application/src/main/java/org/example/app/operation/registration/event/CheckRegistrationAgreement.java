package org.example.app.operation.registration.event;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Agreement;
import org.example.logger.LogIt;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckRegistrationAgreement {

    @LogIt
    public Agreement check(Agreement agreement) {
        log.info("Check agreement № {}", agreement.getNumber());

        return agreement;
    }
}
