package org.example.app.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import static org.testng.Assert.assertEquals;

@Ignore
@SpringBootTest(
    classes = {
        SyncJmsConfiguration.class
    }
)
@ComponentScan(basePackages = {"org.example.app.jms"})
@Slf4j
public class SyncJmsSpringBootTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JmsTemplate jmsTemplate;

    private final ReentrantLock lock = new ReentrantLock();

    @Test(threadPoolSize = 4, invocationCount = 10, timeOut = 5000)
    public void send() {

        String sendMessage = UUID.randomUUID().toString();
        log.info(">>> ------- {}  ------- >>>", sendMessage);

        lock.lock();
        jmsTemplate.convertAndSend("superqueue", sendMessage);
        Object receiveMessage = jmsTemplate.receiveAndConvert("superqueue");
        lock.unlock();

        log.info("<<< ------- {}  ------- <<<", receiveMessage);
        assertEquals(sendMessage, receiveMessage);

    }
}
