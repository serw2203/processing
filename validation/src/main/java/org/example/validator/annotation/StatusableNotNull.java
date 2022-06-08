package org.example.validator.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface StatusableNotNull {
    String[] statuses();
}
