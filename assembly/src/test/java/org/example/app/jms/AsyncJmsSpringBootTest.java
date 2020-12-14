package org.example.app.jms;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.core.JmsTemplate;

import java.util.UUID;

@Disabled
@SpringBootTest(
    classes = {
        AsyncJmsConfiguration.class
    }
)
@ComponentScan(basePackages = {"org.example.app.jms"})
@Slf4j
public class AsyncJmsSpringBootTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private AnnotationJmsListener annotationJmsListener;

    @Disabled
    @Test
    public void send() throws InterruptedException {
        String sendMessage = UUID.randomUUID().toString();
        log.info(">>> ------- {}  ------- >>>", sendMessage);
        this.jmsTemplate.convertAndSend("superqueue", sendMessage);
        Object receiveMessage = this.annotationJmsListener.message(1);
        log.info("<<< ------- {}  ------- <<<", receiveMessage);
        Assertions.assertEquals(sendMessage, receiveMessage);
    }
}
