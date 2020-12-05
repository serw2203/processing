package org.example.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Agreement;
import org.example.app.service.RegistrationAgreementService;
import org.example.logger.LogIt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/agreement")
@RequiredArgsConstructor
@Slf4j
public class AgreementEndpoint {

    private final RegistrationAgreementService registrationAgreementService;

    @GetMapping(value = "/registration", produces = "application/json")
    @LogIt
    public @ResponseBody
    ResponseEntity<Optional<Agreement>> registration() {
        return ResponseEntity.ok(this.registrationAgreementService.registration());
    }
}
