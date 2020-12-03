package org.example.auth;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class AuthLoginModule implements LoginModule {

    private CallbackHandler handler;

    @Getter
    private Subject subject;

    private AuthPrincipal principal;

    private NameCallback nameCallback;

    private PasswordCallback passwordCallback;

    private Callback[] callbacks;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        log.info("AuthLoginModule.initialize ...");
        this.subject = subject;
        this.handler = callbackHandler;
        this.nameCallback = new NameCallback("Name: ");
        this.passwordCallback = new PasswordCallback("Password: ", false);
        this.callbacks = new Callback[]{this.nameCallback, this.passwordCallback};
    }

    @Override
    public boolean login() throws LoginException {
        log.info("AuthLoginModule.login ...");
        int authenticationTimes = 3;

        while (authenticationTimes-- > 0) {
            if (credentialsCheck()) {
                log.info("authentication SUCCESS ...");
                return true;
            }
        }

        log.debug("authentication FAIL ...");
        throw new FailedLoginException();
    }

    @SneakyThrows({IOException.class, UnsupportedCallbackException.class})
    private boolean credentialsCheck() {
        this.handler.handle(this.callbacks);

        String name = this.nameCallback.getName();
        String password = new String(this.passwordCallback.getPassword());

        this.principal = AuthPrincipal.builder().name(name).build();

        return "user".equals(name) && "123".equals(password);
    }

    @Override
    public boolean commit() throws LoginException {
        log.info("AuthLoginModule.commit");
        return subject.getPrincipals().add(this.principal);
    }

    @Override
    public boolean abort() throws LoginException {
        log.info("AuthLoginModule.abort");
        subject.getPrincipals().remove(this.principal);
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        log.info("AuthLoginModule.logout");
        subject.getPrincipals().remove(this.principal);
        return true;
    }
}
