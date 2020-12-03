package org.example.auth;

import lombok.extern.slf4j.Slf4j;

import javax.security.auth.callback.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class AuthCallbackHandler implements CallbackHandler {

    private BufferedReader reader() {
        return new BufferedReader(
                new InputStreamReader(System.in)
        );
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException {
        log.info("AuthCallbackHandler.handle");

        NameCallback nameCallback = (NameCallback) callbacks[0];
        log.info(nameCallback.getPrompt());
        nameCallback.setName(reader().readLine());

        PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
        log.info(passwordCallback.getPrompt());
        passwordCallback.setPassword(reader().readLine().toCharArray());

    }
}
