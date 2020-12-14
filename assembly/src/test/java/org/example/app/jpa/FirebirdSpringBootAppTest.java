package org.example.app.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Disabled
@SpringBootTest(classes = {FirebirdConfiguration.class})
public class FirebirdSpringBootAppTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void init () {
        Assertions.assertEquals(1, this.entityManager.createNativeQuery("select count(1) from dual;").getSingleResult());
    }
}
