package org.qazcodenarxoz.sender;

import org.qazcodenarxoz.annotation.ChannelHandler;
import org.qazcodenarxoz.notification.Notification;

import java.util.concurrent.ThreadLocalRandom;

@ChannelHandler("SMS")
public class SMSSender implements Sender<Notification> {

    @Override
    public void send(Notification notification) throws Exception {

        long start = System.currentTimeMillis();

        Thread.sleep(ThreadLocalRandom.current().nextInt(20, 100));

        if (ThreadLocalRandom.current().nextInt(10) == 0) {
            System.out.println("SEND FAIL id=" + notification.getId()
                    + " channel=SMS thread=" + Thread.currentThread().getName()
                    + " error=SIMULATED_FAIL");
            throw new RuntimeException("SIMULATED_FAIL");
        }

        long duration = System.currentTimeMillis() - start;

        System.out.println("SEND OK id=" + notification.getId()
                + " channel=SMS thread=" + Thread.currentThread().getName()
                + " duration=" + duration + "ms");
    }
}
