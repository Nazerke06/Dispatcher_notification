package org.qazcodenarxoz.sender;

import org.qazcodenarxoz.annotation.ChannelHandler;
import org.qazcodenarxoz.notification.Notification;

import java.util.concurrent.ThreadLocalRandom;

@ChannelHandler("EMAIL")
public class EmailSender implements Sender<Notification> {

    @Override
    public void send(Notification notification) throws Exception {
        simulateWork(notification);
    }

    private void simulateWork(Notification notification) throws Exception {
        long start = System.currentTimeMillis();

        Thread.sleep(ThreadLocalRandom.current().nextInt(20, 100));

        if (ThreadLocalRandom.current().nextInt(10) == 0) {
            throw new RuntimeException("SIMULATED_FAIL");
        }

        long duration = System.currentTimeMillis() - start;

        System.out.println("SEND OK id=" + notification.getId()
                + " channel=EMAIL thread=" + Thread.currentThread().getName()
                + " duration=" + duration + "ms");
    }
}

