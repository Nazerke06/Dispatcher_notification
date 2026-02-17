package org.qazcodenarxoz.dispatcher;

import org.qazcodenarxoz.metrics.Metrics;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;
import org.qazcodenarxoz.sender.Sender;
import org.qazcodenarxoz.util.SenderRegistry;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Dispatcher<T extends Notification> {

    private final NotificationRepository<T> repository;
    private final SenderRegistry registry;

    private Metrics lastMetrics;

    public Dispatcher(NotificationRepository<T> repository,
                      SenderRegistry registry) {
        this.repository = repository;
        this.registry = registry;
    }
    public void printStats() {
        if (lastMetrics != null) {
            lastMetrics.printStats();
        } else {
            System.out.println("No metrics available. Please run send first.");
        }
    }


    public void sendAll(int threads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        List<T> notifications = repository.getAll();

        Metrics metrics = new Metrics();

        for (T notification : notifications) {
            executor.submit(() -> {
                long start = System.currentTimeMillis();
                try {
                    Optional<Sender<?>> senderOpt =
                            registry.getSender(notification.getChannel());

                    if (senderOpt.isEmpty()) {
                        throw new RuntimeException("NO_SENDER");
                    }

                    Sender<T> sender = (Sender<T>) senderOpt.get();
                    sender.send(notification);

                    metrics.recordSuccess(notification,
                            System.currentTimeMillis() - start);

                } catch (Exception e) {
                    metrics.recordFail(notification, e.getMessage());
                }
            });
        }


        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        this.lastMetrics = metrics;
    }
}