package org.qazcodenarxoz.dispatcher.strategy;


import org.qazcodenarxoz.dispatcher.DispatchStrategy;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.util.SenderRegistry;

import java.util.Queue;

public class SingleThreadStrategy implements DispatchStrategy {
    @Override
    public void dispatch(Queue<Notification> queue, Dispatcher.Metrics metrics, SenderRegistry registry) {
        Notification notification;
        while ((notification = queue.poll()) != null) {
            Dispatcher.processTask(notification, metrics, registry);
        }
    }
}