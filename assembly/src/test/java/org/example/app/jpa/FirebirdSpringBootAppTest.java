package org.example.app.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Disabled
@SpringBootTest(classes = {FirebirdConfiguration.class})
@ComponentScan(basePackages = {"org.example.app.jpa"})
public class FirebirdSpringBootAppTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void init () {
        Assertions.assertEquals(1, this.entityManager.createNativeQuery("select count(1) from dual;").getSingleResult());
    }
}
