package org.example.app.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.example.app.operation.registration.RegistrationAgreementOperation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class RegistrationAgreementService {
    @Getter(AccessLevel.NONE)
    @Delegate
    private final RegistrationAgreementOperation registrationOperation;


}
