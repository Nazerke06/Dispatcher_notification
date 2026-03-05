package org.qazcodenarxoz.sender;

import org.qazcodenarxoz.annotation.ChannelHandler;
import org.qazcodenarxoz.notification.Notification;

import java.util.concurrent.ThreadLocalRandom;

@ChannelHandler("WHATSAPP")
public class WhatsAppSender implements Sender<Notification> {

    @Override
    public void send(Notification notification) throws Exception {
        Thread.sleep(ThreadLocalRandom.current().nextInt(20, 100));

        if (ThreadLocalRandom.current().nextInt(10) == 0) {
            throw new RuntimeException("SIMULATED_FAIL");
        }
    }
}
