package org.example.app.jpa;

import org.example.app.jpa.firebird_entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.PersistenceException;

// @Disabled
@SpringBootTest(classes = {FirebirdConfiguration.class, FirebirdServiceConfiguration.class})
@ComponentScan(basePackages = {"org.example.app.jpa"})
public class FirebirdSpringBootAppTest {

    @Autowired
    private FirebirdServiceConfiguration.EmployeeService employeeService;

    @Test
    public void employeeService$persist() {
        Employee employee = Employee.builder().lastName("Бархатов")
            .firstName("Сергей")
            .middleName("Владимирович")
            .id(1L)
            .build();
        Assertions.assertThrows(PersistenceException.class, () -> employeeService.persist(employee));
    }

    @Test
    public void employeeService$merge() {
        Employee employee = Employee.builder().lastName("Бархатов")
            .firstName("Сергей")
            .middleName("Владимирович")
            .id(1L)
            .build();

        employeeService.merge(employee);
    }
}
