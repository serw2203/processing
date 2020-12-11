package org.example.app.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class AsyncJmsConfiguration {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        return new ActiveMQConnectionFactory(this.brokerUrl);
    }

    @Primary
    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        return new CachingConnectionFactory(this.activeMQConnectionFactory());
    }

    @Primary
    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(this.cachingConnectionFactory());
    }


    @Bean
    public SingleConnectionFactory singleConnectionFactory() {
        return new SingleConnectionFactory(this.activeMQConnectionFactory());
    }

    @Bean
    public JmsTemplate jmsTemplateSingle() {
        return new JmsTemplate(this.singleConnectionFactory());
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(this.activeMQConnectionFactory());
        return factory;
    }

    @Bean
    public AnnotationJmsListener annotationJmsListener() {
        return new AnnotationJmsListener();
    }


}
