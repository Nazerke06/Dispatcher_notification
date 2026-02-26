package org.qazcodenarxoz.sender;

import lombok.extern.slf4j.Slf4j;
import org.qazcodenarxoz.annotation.ChannelHandler;
import org.qazcodenarxoz.notification.Notification;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@ChannelHandler("EMAIL")
public class EmailSender implements Sender<Notification> {

    @Override
    public void send(Notification notification) throws Exception {
        simulateWork(notification);
    }

    private void simulateWork(Notification notification) throws Exception {
//        long start = System.currentTimeMillis();

        Thread.sleep(ThreadLocalRandom.current().nextInt(20, 100));

        if (ThreadLocalRandom.current().nextInt(10) == 0) {
            throw new RuntimeException("SIMULATED_FAIL");
        }

//        long duration = System.currentTimeMillis() - start;
//
//        log.info("SEND OK id=" + notification.getId()
//                + " channel=EMAIL thread=" + Thread.currentThread().getName()
//                + " duration=" + duration + "ms");
    }
}

