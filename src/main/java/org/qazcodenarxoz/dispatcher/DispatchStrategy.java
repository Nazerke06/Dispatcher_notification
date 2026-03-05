package org.qazcodenarxoz.dispatcher;

import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.util.SenderRegistry;
import java.util.Queue;

public interface DispatchStrategy {
    void dispatch(Queue<Notification> queue, Dispatcher.Metrics metrics, SenderRegistry registry) throws InterruptedException;
}
