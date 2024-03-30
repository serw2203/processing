package org.example.app.jms;

import jakarta.jms.*;
import lombok.extern.slf4j.Slf4j;


@Deprecated
@Slf4j
public class ExtendedMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            String msg = ((TextMessage) message).getText();
            log.info("||| ------- {}  ------- |||", msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
