package org.example.app.jpa;

import org.example.app.jpa.entity.LegalParty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;

@Configuration
public class ServiceConfiguration {

    @Service
    @Transactional
    public static class LegalPartyService {
        @PersistenceContext
        private EntityManager entityManager;

        public void persist(LegalParty legalParty) {
            this.entityManager.persist(legalParty);
        }

        public BigInteger count () {
            return (BigInteger) this.entityManager
                .createNativeQuery("select count(*) from legal_party")
                .getSingleResult();
        }
    }

    @Bean
    public LegalPartyService legalPartyService() {
        return new LegalPartyService();
    }

}
