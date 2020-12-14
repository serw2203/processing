package org.example.app.operation.registration.event;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Agreement;
import org.example.logger.LogIt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@Slf4j
public class CreateAgreement {

    @LogIt
    public Agreement create() {
        Agreement.AgreementState agreementState = Agreement.AgreementState.builder()
            .number(UUID.randomUUID().toString())
            .attr1(UUID.randomUUID().toString())
            .attr2("ATTR2 ------------ <V7> --------------")
            .documentDate(LocalDate.now())
            .build();

        Agreement agreement = Agreement.builder()
            .id(UUID.randomUUID().toString())
            .state(agreementState)
            .build();

        log.info("Check agreement № {}", agreement.getNumber());

        return agreement;
    }

}
