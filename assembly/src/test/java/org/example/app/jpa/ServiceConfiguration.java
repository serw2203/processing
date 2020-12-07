package org.example.app.jpa;

import org.example.app.jpa.entity.LegalParty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;

@Configuration
public class ServiceConfiguration {


    public static class LegalPartyService {
        @PersistenceContext
        private EntityManager entityManager;
        @Transactional
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
