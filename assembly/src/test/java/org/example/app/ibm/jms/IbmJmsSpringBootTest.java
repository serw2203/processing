package org.example.app.ibm.jms;

import com.ibm.mq.jms.MQQueue;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Disabled
@SpringBootTest(classes = {IbmJmsConfiguration.class})
@ComponentScan(basePackages = {"org.example.app.ibm.jms"})
@Slf4j
public class IbmJmsSpringBootTest {

    @Autowired
    public JmsTemplate jmsTemplate;

    @Autowired
    public ConnectionFactory connectionFactory;

//    @Test
//    public void test_0() {
//        jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello");
//        String response = (String) jmsTemplate.receiveAndConvert("DEV.QUEUE.1");
//        Assertions.assertEquals("Hello", response);
//    }

    @SneakyThrows
    @Test
    public void test_1() {
        try (
            Connection connection = this.connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(session.createQueue("DEV.QUEUE.2"));
            MessageConsumer consumer = session.createConsumer(session.createQueue("DEV.QUEUE.2"))) {

            Message message = session.createTextMessage("Hello");
            message.setJMSCorrelationID(UUID.randomUUID().toString());
            message.setJMSReplyTo(new MQQueue("DEV.QUEUE.3"));
//            message.setJMSExpiration(1L);
            producer.send(message);

            connection.start();

            try {
                TextMessage textMessage = (TextMessage) consumer.receive(1L);
                Assertions.assertEquals("Hello", textMessage.getText());
                Assertions.assertEquals(message.getJMSCorrelationID(), textMessage.getJMSCorrelationID());
            } finally {
                connection.stop();
            }
        }
    }

    @SneakyThrows
    @Test
    public void test_2() {
        try (
            Connection connection = this.connectionFactory.createConnection();
            Session session = connection.createSession();
            MessageProducer producer = session.createProducer(session.createQueue("DEV.QUEUE.3"))) {

            Message message = session.createTextMessage("Hello");

            final CompletableFuture<TextMessage> completableFuture = new CompletableFuture<>();

            producer.send(message, new CompletionListener() {
                @Override
                public void onCompletion(Message message) {
                    completableFuture.completeOnTimeout((TextMessage) message, 1L, TimeUnit.MILLISECONDS);
                }

                @Override
                public void onException(Message message, Exception exception) {
                    completableFuture.completeExceptionally(exception);
                }
            });

            Assertions.assertEquals("Hello", completableFuture.get().getText());
        }
    }

    @Autowired
    public IbmJmsConfiguration.Receiver receiver;

    @Disabled
    @SneakyThrows
    @Test
    public void test_3() {
        receiver.setCompletableFuture(new CompletableFuture<>());
        jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello");

        Assertions.assertEquals("Hello",
            receiver.getCompletableFuture().get(10L, TimeUnit.MILLISECONDS).getText()
        );

        receiver.setCompletableFuture(new CompletableFuture<>());
        jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello");
        Assertions.assertEquals("Hello",
            receiver.getCompletableFuture().get(10L, TimeUnit.MILLISECONDS).getText()
        );
    }

}
