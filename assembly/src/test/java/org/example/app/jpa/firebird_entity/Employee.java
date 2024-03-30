package org.example.app.jpa.firebird_entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

//import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "A_EMPLOYEE", schema = "", catalog = "")
public class Employee extends EntityObject {
    private String firstName;
    private String lastName;
    private String middleName;
    private String random;

    @Builder
    public Employee(String firstName, String lastName, String middleName, Long id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.random = UUID.randomUUID().toString();
        this.setId(id);
    }
}
