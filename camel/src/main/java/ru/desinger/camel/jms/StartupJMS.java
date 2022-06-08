package ru.desinger.camel.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
@EnableJms
@ComponentScan(basePackages = {"ru.desinger.camel.jms"})
public class StartupJMS {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(this.brokerUrl);
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        return new CachingConnectionFactory(this.connectionFactory());
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(this.cachingConnectionFactory());
    }

    @Bean
    public DefaultJmsListenerContainerFactory listenerConnectionFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(this.connectionFactory());
        return factory;
    }

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource("jdbc:oracle:thin:hr/hr@minikube:32619:xe");
    }

//    @Bean
//    public AnnotationJmsListener annotationJmsListener() {
//        return new AnnotationJmsListener();
//    }
//
//    @Component
//    public static class AnnotationJmsListener {
//        @JmsListener(destination = "superqueue")
//        public void receive(String message) {
//            System.out.println("LLL -- " + message + "   " + Thread.currentThread());
//        }
//    }

    @Component
    public static class SendHelloJMSRouter extends RouteBuilder {
        @Value("${destination:superqueue}")
        private String destination;

        @Value("${spring.activemq.broker-url}")
        private String brokerUrl;

//        @Autowired
//        private ActiveMQComponent activemq;

        @Override
        public void configure() {
            restConfiguration().component("undertow").host("localhost").port(8081).bindingMode(RestBindingMode.auto);

            errorHandler(defaultErrorHandler());

            onException(Exception.class).log("----> ${body}");

            rest("/hello").get()
                .route().id("hello")
                .transform(constant("Hello from startup JMS"))
                .process((e) -> {
                    System.out.println("====>>>> " + e.getIn().getHeaders());
                })
                .to("activemq:queue:" + this.destination /*+ "?disableReplyTo=true"*/)
                .process((e) -> {
                    System.out.println("====<<<< " + e.getIn().getHeaders());
                })
                .log(">>>> ${body}")
                .endRest();


            from("activemq:queue:" + this.destination)
                .log("<<<< ${body}")
                .end();
        }
    }

    //@Component
    public static class DatabaseRouter extends RouteBuilder {

        @Autowired
        public DataSource dataSource;

        @Override
        public void configure() {

            getContext().getRegistry().bind("ds", this.dataSource);

            from("timer:base?period=10000")
                .routeId("jdbc-countries")
                .setBody(simple("select country_id, country_name from hr.countries"))
                .to("jdbc:ds")
                .log(">->-> ${body}")
                .end();
        }
    }

/*
    @Component
    public static class Receiver {
        @JmsListener(containerFactory = "jmsListenerContainerFactory", destination = "DEV.QUEUE.3")
        public void receive(String message) {
            System.out.println(message + "   " + Thread.currentThread());
        }
    }

   @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                      DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        return jmsTemplate;
    }

    @Primary
    @Bean
    public MQConnectionFactory connectionFactory() {
        try {
            MQConnectionFactory connectionFactory = new MQConnectionFactory();
            connectionFactory.setHostName("minikube");
            connectionFactory.setPort(31515);
            connectionFactory.setQueueManager("Q1");
            connectionFactory.setChannel("DEV.ADMIN.SVRCONN");
            connectionFactory.setTransportType(CommonConstants.WMQ_CLIENT_NONJMS_MQ);
            connectionFactory.setCCSID(JmsConstants.CCSID_UTF8);
//            connectionFactory.
            return connectionFactory;
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Bean
    public UserCredentialsConnectionFactoryAdapter credentialsConnectionFactoryAdapter() {

        UserCredentialsConnectionFactoryAdapter connectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
        connectionFactoryAdapter.setTargetConnectionFactory(connectionFactory());
        connectionFactoryAdapter.setUsername("admin");
        connectionFactoryAdapter.setPassword("passw0rd");
        return connectionFactoryAdapter;
    }

    @Bean
    public JmsComponent jmsComponent(ConnectionFactory connectionFactory) {
        return JmsComponent.jmsComponentAutoAcknowledge(connectionFactory);
    }
    */


    public static void main(String[] args) throws SQLException {
        ConfigurableApplicationContext context = SpringApplication.run(StartupJMS.class, args);
//        context.getBean(JmsTemplate.class).convertAndSend("superqueue", "HELLO !!!");

//        DataSource dataSource = context.getBean(DataSource.class);
//        Connection connection = dataSource.getConnection();
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery("select country_id from hr.countries");
//
//        while (resultSet.next()) {
//            System.out.println(resultSet.getString(1));
//        }
    }
}
