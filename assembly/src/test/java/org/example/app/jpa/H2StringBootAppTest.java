package org.example.app.jpa;

import org.example.app.jpa.h2entity.Employee;
import org.example.app.jpa.h2entity.LegalParty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

@SpringBootTest(classes = {H2Configuration.class, H2ServiceConfiguration.class})
public class H2StringBootAppTest {

    @Autowired
    private H2ServiceConfiguration.LegalPartyService legalPartyService;

    @Autowired
    private H2ServiceConfiguration.EmployeeService employeeService;

    @Test
    @Order(0)
    public void shouldInitLegalParty() {
        Assertions.assertEquals(BigInteger.ONE, this.legalPartyService.count());
    }

    @Test
    @Order(0)
    public void shouldInitEmployee() {
        Assertions.assertEquals(BigInteger.ONE, this.employeeService.count());
    }

    @Test
    public void shouldInsertLegalParty() {
        BigInteger count = this.legalPartyService.count();

        this.legalPartyService.persist(new LegalParty().withName("New Legal Party")
            .withAccount("12345678")
            .withInn("12345")
        );

        Assertions.assertEquals(count.add(BigInteger.ONE), this.legalPartyService.count());
    }

    @Test
    public void shouldInsertEmployee() {
        BigInteger count = this.employeeService.count();

        this.employeeService.persist(new Employee().withLastName("СТЕПАНОВ")
            .withFirstName("СТЕПАН")
            .withMiddleName("СТЕПАНОВИЧ")
        );

        Assertions.assertEquals(count.add(BigInteger.ONE), this.employeeService.count());
    }

    @Test
    public void shouldRollbackInsertWithUncheckedException() {
        BigInteger count = this.employeeService.count();

        try {
            this.employeeService.persist(new Employee().withLastName("СТЕПАНОВ")
                    .withFirstName("СТЕПАН")
                    .withMiddleName("СТЕПАНОВИЧ"),
                () -> {
                    throw new RuntimeException("Hello");
                }
            );
        } catch (RuntimeException re) {
            Assertions.assertEquals(re.getMessage(), "Hello");
        }
        Assertions.assertEquals(count, this.employeeService.count());
    }

    //WTF ???
    @Test
    public void shouldRollbackInsertWithError() {
        BigInteger count = this.employeeService.count();

        try {
            this.employeeService.persist(new Employee().withLastName("СТЕПАНОВ")
                    .withFirstName("СТЕПАН")
                    .withMiddleName("СТЕПАНОВИЧ"), () -> {
                    throw new Error("Hello");
                }
            );
        } catch (Throwable te) {
            Assertions.assertEquals(te.getMessage(), "Hello");
        }

        Assertions.assertEquals(count, this.employeeService.count());
    }

    @Test
    public void shouldRollbackInsertEmployeeWithCheckedException() {
        BigInteger count = this.employeeService.count();

        try {
            this.employeeService.persist2(new Employee().withLastName("СТЕПАНОВ")
                    .withFirstName("СТЕПАН")
                    .withMiddleName("СТЕПАНОВИЧ"), () -> {
                    throw new Exception("Hello");
                }
            );
        } catch (Throwable te) {
            Assertions.assertEquals(te.getMessage(), "Hello");
        }

        Assertions.assertEquals(count.add(BigInteger.ONE), this.employeeService.count());
    }

    @Test
    public void shouldRollbackInnerAndRollbackOuterInsertWithUncheckedException() {
        BigInteger employeeCount = this.employeeService.count();
        BigInteger legalPartyCount = this.legalPartyService.count();

        try {
            this.legalPartyService.persist(new LegalParty().withName("name"),
                () -> this.employeeService.persist(new Employee().withLastName("LAST"), () -> {
                        throw new RuntimeException("Hello");
                    }
                )
            );
        } catch (RuntimeException re) {
            Assertions.assertEquals(re.getMessage(), "Hello");
        }

        Assertions.assertEquals(employeeCount, this.employeeService.count());
        Assertions.assertEquals(legalPartyCount, this.legalPartyService.count());
    }

    @Test
    public void shouldCommitInnerAndCommitOuterInsertWithCheckedException() {
        BigInteger employeeCount = this.employeeService.count();
        BigInteger legalPartyCount = this.legalPartyService.count();

        try {
            this.legalPartyService.persist2(new LegalParty().withName("name"),
                () -> this.employeeService.persist2(new Employee().withLastName("LAST"), () -> {
                        throw new Exception("Hello");
                    }
                )
            );
        } catch (Exception re) {
            Assertions.assertEquals(re.getMessage(), "Hello");
        }

        Assertions.assertEquals(employeeCount.add(BigInteger.ONE), this.employeeService.count());
        Assertions.assertEquals(legalPartyCount.add(BigInteger.ONE), this.legalPartyService.count());
    }
}
