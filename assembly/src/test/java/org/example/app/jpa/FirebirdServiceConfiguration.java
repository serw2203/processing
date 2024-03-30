package org.example.app.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.app.jpa.firebird_entity.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;

@Configuration
public class FirebirdServiceConfiguration {

    public interface EmployeeService {
        void persist(Employee employee);

        void merge(Employee employee);
    }

    @Transactional
    public static class EmployeeServiceImpl implements EmployeeService {
        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public void persist(Employee employee) {
            this.entityManager.persist(employee);
        }

        @Override
        public void merge(Employee employee) {
            this.entityManager.merge(employee);
        }
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeServiceImpl();
    }
}
