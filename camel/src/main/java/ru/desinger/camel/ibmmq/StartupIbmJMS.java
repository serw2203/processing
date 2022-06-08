package ru.desinger.camel.ibmmq;


import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.jms.JmsConstants;
import com.ibm.msg.client.wmq.WMQConstants;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.stereotype.Component;


import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication
@EnableJms
@ComponentScan(basePackages = {"ru.desinger.camel.ibmmq"})
public class StartupIbmJMS {

    @Value("${ibm.mq.host}")
    private String hostName;

    @Value("${ibm.mq.port}")
    private Integer port;

    @Value("${ibm.mq.queueManager}")
    private String queueManager;

    @Value("${ibm.mq.channel}")
    private String channel;

    @Bean
    @SneakyThrows
    public MQConnectionFactory mqConnectionFactory() {
        MQConnectionFactory connectionFactory = new MQConnectionFactory();
        connectionFactory.setHostName(this.hostName);
        connectionFactory.setPort(this.port);
        connectionFactory.setQueueManager(this.queueManager);
        connectionFactory.setChannel(this.channel);
        connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        connectionFactory.setCCSID(JmsConstants.CCSID_UTF8);
        return connectionFactory;
    }

    @Primary
    @Bean
    public SingleConnectionFactory connectionFactory(UserCredentialsConnectionFactoryAdapter adapter) {
        SingleConnectionFactory connectionFactory = new SingleConnectionFactory();
        connectionFactory.setTargetConnectionFactory(adapter);
        return connectionFactory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        return new CachingConnectionFactory(this.mqConnectionFactory());
    }

    @Bean
    public UserCredentialsConnectionFactoryAdapter credentialsConnectionFactoryAdapter(
        @Value("${ibm.mq.user}") String user,
        @Value("${ibm.mq.password}") String password) {

        UserCredentialsConnectionFactoryAdapter connectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
        connectionFactoryAdapter.setTargetConnectionFactory(mqConnectionFactory());
        connectionFactoryAdapter.setUsername(user);
        connectionFactoryAdapter.setPassword(password);
        return connectionFactoryAdapter;
    }

//    @Bean
//    public DefaultJmsListenerContainerFactory listenerConnectionFactory(ConnectionFactory connectionFactory) {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        return factory;
//    }

    @Bean
    public JmsComponent ibmmq(ConnectionFactory connectionFactory) {
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConnectionFactory(connectionFactory);
        return jmsComponent;
    }

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource("jdbc:oracle:thin:hr/hr@minikube:32619:xe");
    }

    @Component
    public static class SendHelloJMSRouter extends RouteBuilder {
        @Value("${ibm.mq.destination}")
        private String destination;

        @Override
        public void configure() {
            //alter user hr identified by hr account unlock;
            restConfiguration().component("undertow").host("localhost").port(8082).bindingMode(RestBindingMode.auto);

            rest("/hello").get()
                .route().id("hello")
                .transform(constant("Hello from startup JMS"))
                .to("sql:select * from countries?dataSource=dataSource")
                .split(body())
                .process(exchange -> {
                    System.out.println(":: = " + exchange.getIn().getBody());
                })
                .log(" ===---=== > ${body}")
                .convertBodyTo(String.class)
                .to("ibmmq:queue:" + this.destination )
                .end()
                .log(" < ===---=== ")
                .setBody(constant("OK"))
                .to("mock:result")
                .endRest();


            from("ibmmq:queue:" + this.destination + "?asyncConsumer=true")
                .log("<<<< ${body}")
                .delay(simple("10000"))
                .end();
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StartupIbmJMS.class, args);
    }
}
