package org.example.app.jpa;

import org.example.app.jpa.h2entity.Employee;
import org.example.app.jpa.h2entity.LegalParty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;

@Configuration
public class H2ServiceConfiguration {

    public interface RCE {
        void run () throws Exception;
    }

    public interface LegalPartyService {
        <C extends Runnable> void persist(LegalParty legalParty, C callback);

        <C extends RCE> void persist2(LegalParty legalParty, C callback) throws Exception;

        void persist(LegalParty legalParty);

        BigInteger count();
    }

    public interface EmployeeService {
        <C extends Runnable> void persist(Employee legalParty, C callback);

        <C extends RCE> void persist2(Employee legalParty, C callback) throws Exception;

        void persist(Employee legalParty);

        BigInteger count();
    }

    @Transactional
    public static class LegalPartyServiceImpl implements LegalPartyService {

        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public void persist(LegalParty legalParty, Runnable callback) {
            this.entityManager.persist(legalParty);
            if (callback != null) callback.run();
        }

        @Override
        public void persist(LegalParty legalParty) {
            this.persist(legalParty, null);
        }

        @Override
        public <C extends RCE> void persist2(LegalParty legalParty, C callback) throws Exception {
            this.entityManager.persist(legalParty);
            if (callback != null) callback.run();
        }

        public BigInteger count() {
            return (BigInteger) this.entityManager
                .createNativeQuery("select count(*) from legal_party")
                .getSingleResult();
        }
    }

    @Transactional
    public static class EmployeeServiceImpl implements EmployeeService {

        @PersistenceContext
        private EntityManager entityManager;


        @Override
        public void persist(Employee employee, Runnable callback) {
            this.entityManager.persist(employee);
            if (callback != null) callback.run();
        }

        @Override
        public void persist(Employee employee) {
            this.persist(employee, null);
        }

        @Override
        public <C extends RCE> void persist2(Employee legalParty, C callback) throws Exception {
            this.entityManager.persist(legalParty);
            if (callback != null) callback.run();
        }

        public BigInteger count() {
            return (BigInteger) this.entityManager
                .createNativeQuery("select count(*) from employee")
                .getSingleResult();
        }
    }

    @Bean
    public LegalPartyService legalPartyService() {
        return new LegalPartyServiceImpl();
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeServiceImpl();
    }

}
