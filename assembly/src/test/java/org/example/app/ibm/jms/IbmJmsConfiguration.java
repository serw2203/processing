package org.example.app.ibm.jms;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.jms.JmsConstants;
import com.ibm.msg.client.wmq.common.CommonConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.TextMessage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@EnableJms
public class IbmJmsConfiguration {

    @Value("${ibm.mq.host}")
    private String hostName;

    @Value("${ibm.mq.port}")
    private Integer port;

    @Value("${ibm.mq.queueManager}")
    private String queueManager;

    @Value("${ibm.mq.channel}")
    private String channel;

    @SneakyThrows
    @Bean
    public MQConnectionFactory connectionFactory() {
        MQConnectionFactory connectionFactory = new MQConnectionFactory();
        connectionFactory.setHostName(this.hostName);
        connectionFactory.setPort(this.port);
        connectionFactory.setQueueManager(this.queueManager);
        connectionFactory.setChannel(this.channel);
        connectionFactory.setTransportType(CommonConstants.WMQ_CLIENT_NONJMS_MQ);
        connectionFactory.setCCSID(JmsConstants.CCSID_UTF8);
        return connectionFactory;
    }

    @Primary
    @Bean
    public UserCredentialsConnectionFactoryAdapter credentialsConnectionFactoryAdapter(
        @Value("${ibm.mq.user}") String user,
        @Value("${ibm.mq.password}") String password) {

        UserCredentialsConnectionFactoryAdapter connectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
        connectionFactoryAdapter.setTargetConnectionFactory(connectionFactory());
        connectionFactoryAdapter.setUsername(user);
        connectionFactoryAdapter.setPassword(password);
        return connectionFactoryAdapter;
    }

    @Bean
    public JmsTemplate jmsTemplate(UserCredentialsConnectionFactoryAdapter connectionFactoryAdapter) {
        return new JmsTemplate(connectionFactoryAdapter);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(UserCredentialsConnectionFactoryAdapter factoryAdapter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(factoryAdapter);
        return factory;
    }

//    public static class Receiver {
//        @JmsListener(destination = "DEV.QUEUE.1")
//        public void onMessage(TextMessage message) {
//            System.out.println("from template == " + message);
//        }
//    }

//    @Bean
//    public Receiver receiver() {
//        return new Receiver();
//    }

}
