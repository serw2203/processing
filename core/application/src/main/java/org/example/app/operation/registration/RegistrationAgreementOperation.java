package org.example.app.operation.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.model.Agreement;
import org.example.app.operation.registration.event.CheckRegistrationAgreement;
import org.example.app.operation.registration.event.CreateAgreement;
import org.example.app.operation.registration.event.DefineTariffRegistrationAgreement;
import org.example.logger.LogIt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Getter
@RequiredArgsConstructor
public class RegistrationAgreementOperation {

    private final CreateAgreement createAgreement;
    private final DefineTariffRegistrationAgreement defineTariff;
    private final CheckRegistrationAgreement checkRegistration;

    @LogIt
    public Optional<Agreement> registration() {
        return Optional.of(createAgreement.create())
                .map(this.checkRegistration::check)
                .map(this.defineTariff::define);
    }

}
