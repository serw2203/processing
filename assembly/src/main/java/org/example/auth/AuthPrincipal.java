package org.example.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;
import java.security.Principal;
import java.util.Objects;

@Builder
public class AuthPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @NonNull
    private final String name;

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        return obj instanceof AuthPrincipal && obj.toString().equals(this.name);
    }
}
