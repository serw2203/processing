package org.example.app.jms;

import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

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
