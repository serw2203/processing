package org.example.app.jpa.h2entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

//import javax.persistence.Entity;
//import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "EMPLOYEE")
public class Employee extends EntityObject {
    @With
    private String firstName;
    @With
    private String lastName;
    @With
    private String middleName;

    @Builder
    public Employee(String firstName, String lastName, String middleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }
}
