package org.example.app.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class AnnotationJmsListener {

    private final ReentrantLock lock = new ReentrantLock();
    private volatile String message;
    private volatile CountDownLatch countDownLatch;

    @JmsListener(destination = "superqueue")
    public void processed(String message) {
        lock.lock();
        this.message = message;
        try {
            if (this.countDownLatch != null) {
                this.countDownLatch.countDown();
            } else {
                log.warn("LOST MESSAGE ======== {} ========", message);
            }
        } finally {
            lock.unlock();
        }
    }

    public String message(long timeout) throws InterruptedException {
        this.countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await(timeout, TimeUnit.SECONDS);
            return this.message;
        } finally {
            lock.lock();
            this.message = null;
            this.countDownLatch = null;
            lock.unlock();
        }
    }
}
