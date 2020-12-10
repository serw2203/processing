package org.example.app.jms;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.util.UUID;

@SpringBootTest(
    classes = {
        SyncJmsConfiguration.class
    }
)
@Slf4j
public class SyncJmsSpringBootTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Disabled
    @Test
    public void send() {
        String sendMessage = UUID.randomUUID().toString();
        log.info(">>> ------- {}  ------- >>>", sendMessage);
        this.jmsTemplate.convertAndSend("superqueue", sendMessage);
        Object receiveMessage = this.jmsTemplate.receiveAndConvert("superqueue");
        log.info("<<< ------- {}  ------- <<<", receiveMessage);
        Assertions.assertEquals(sendMessage, receiveMessage);
    }
}
