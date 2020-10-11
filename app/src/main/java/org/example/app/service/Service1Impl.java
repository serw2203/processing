package org.example.app.service;

import lombok.RequiredArgsConstructor;
import org.example.app.model.User;
import org.example.logger.LogIt;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;


@Service
@RequiredArgsConstructor
public class Service1Impl implements Service1 {

    private final Validator validator;

    @LogIt
    public String info() {
        System.out.println(validator);
        return "HELLO FROM SERVICE1";
    }

    @LogIt
    public User user() {
        return User.builder()
                .number("1")
                .firstName("Sergey")
                .lastName("Barkhatov")
                .build();
    }
}
