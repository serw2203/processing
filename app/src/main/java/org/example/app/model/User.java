package org.example.app.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String number;
    private String lastName;
    private String firstName;
    private String middleName;
}
