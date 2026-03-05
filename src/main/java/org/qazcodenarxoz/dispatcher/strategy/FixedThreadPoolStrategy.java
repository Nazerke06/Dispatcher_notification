package org.qazcodenarxoz.dispatcher.strategy;

import org.qazcodenarxoz.dispatcher.DispatchStrategy;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.util.SenderRegistry;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FixedThreadPoolStrategy implements DispatchStrategy {
    private final int threads;

    public FixedThreadPoolStrategy(int threads) {
        this.threads = threads;
    }

    @Override
    public void dispatch(Queue<Notification> queue, Dispatcher.Metrics metrics, SenderRegistry registry) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Notification notification;
        while ((notification = queue.poll()) != null) {
            Notification finalNotification = notification;
            executor.submit(() -> Dispatcher.processTask(finalNotification, metrics, registry));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
