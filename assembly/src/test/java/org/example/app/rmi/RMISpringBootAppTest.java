package org.example.app.rmi;

import org.example.app.service.calculator.CalculateAction;
import org.example.app.service.calculator.CalculateActionAdd;
import org.example.app.service.calculator.CalculateActionMultiple;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
@SpringBootTest(classes = {CalculateConfiguration.class, RMISpringBootAppTest.CalculateClientConfiguration.class})
@ComponentScan(basePackages = {"org.example.app.rmi"})
public class RMISpringBootAppTest {

    @Configuration
    static class CalculateClientConfiguration {
        @Bean
        public RmiProxyFactoryBean proxyCalculateActionAdd() {
            RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
            rmiProxyFactory.setServiceUrl("rmi://localhost:1099/CalculateActionAdd");
            rmiProxyFactory.setServiceInterface(CalculateAction.class);
            return rmiProxyFactory;
        }

        @Bean
        public RmiProxyFactoryBean proxyCalculateActionMultiple() {
            RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
            rmiProxyFactory.setServiceUrl("rmi://localhost:1099/CalculateActionMultiple");
            rmiProxyFactory.setServiceInterface(CalculateAction.class);
            return rmiProxyFactory;
        }
    }

    @Autowired
    @Qualifier("proxyCalculateActionAdd")
    private CalculateAction actionAdd;

    @Autowired
    @Qualifier("proxyCalculateActionMultiple")
    private CalculateAction actionMultiple;

    @DisplayName("Test RMI perform action 'ADD'")
    @Test
    public void test_add() {
        assertEquals(actionAdd.perform(1L, 2L), 3L);
        assertEquals(actionAdd.name(), CalculateActionAdd.ACTION_NAME);
    }

    @DisplayName("Test RMI perform action 'MULTIPLE'")
    @Test
    public void test_multiple() {
        assertEquals(actionMultiple.perform(1L, 2L), 2L);
        assertEquals(actionMultiple.name(), CalculateActionMultiple.ACTION_NAME);
    }

}
