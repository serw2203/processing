package org.example.app;

import org.example.app.config.CalculateConfiguration;
import org.example.app.service.CalculateAction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CalculateConfiguration.class, SpringBootAppTest.TestConfiguration.class})
public class SpringBootAppTest {

    @Configuration
    static class TestConfiguration {
        @Bean
        public RmiProxyFactoryBean proxyCalculateActionAdd () {
            RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
            rmiProxyFactory.setServiceUrl("rmi://localhost:1099/CalculateActionAdd");
            rmiProxyFactory.setServiceInterface(CalculateAction.class);
            return rmiProxyFactory;
        }

        @Bean
        public RmiProxyFactoryBean proxyCalculateActionMultiple () {
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
        assertEquals(actionAdd.perform (1L, 2L), 3L);
    }

    @DisplayName("Test RMI perform action 'MULTIPLE'")
    @Test
    public void test_multiple() {
        assertEquals(actionMultiple.perform (1L, 2L), 2L);
    }

}
