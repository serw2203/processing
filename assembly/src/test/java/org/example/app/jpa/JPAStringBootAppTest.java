package org.example.app.jpa;

import org.example.app.jpa.entity.LegalParty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

@SpringBootTest(classes = {BaseConfiguration.class, ServiceConfiguration.class})
public class JPAStringBootAppTest {

    @Autowired
    private ServiceConfiguration.LegalPartyService legalPartyService;

    @Test
    public void shouldInitLegalParty() {
        Assertions.assertEquals(BigInteger.valueOf(1),
            this.legalPartyService.count()
        );
    }

    @Test
    public void shouldInsertLegalParty() {
        this.legalPartyService.persist(new LegalParty()
            .withName("New Legal Party")
            .withAccount("12345678")
            .withInn("12345")
        );

        Assertions.assertEquals(BigInteger.valueOf(2),
            this.legalPartyService.count()
        );
    }

}
