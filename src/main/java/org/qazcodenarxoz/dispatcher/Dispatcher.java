package org.qazcodenarxoz.dispatcher;

import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.notification.OTPNotification;
import org.qazcodenarxoz.repository.NotificationRepository;
import org.qazcodenarxoz.sender.Sender;
import org.qazcodenarxoz.util.SenderRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Dispatcher<T extends Notification> {

    private final NotificationRepository<T> repository;
    private final SenderRegistry registry;
    private Metrics lastMetrics;

    public Dispatcher(NotificationRepository<T> repository, SenderRegistry registry) {
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
                    Optional<Sender<?>> senderOpt = registry.getSender(notification.getChannel());
                    if (senderOpt.isEmpty()) throw new RuntimeException("NO_SENDER");

                    @SuppressWarnings("unchecked")
                    Sender<T> sender = (Sender<T>) senderOpt.get();
                    sender.send(notification);

                    long duration = System.currentTimeMillis() - start;

                    String typeTag = (notification instanceof OTPNotification) ? "[OTP-CONFIDENTIAL]" : "";

                    System.out.printf("SEND OK id=%d channel=%s thread=%s duration=%dms %s%n",
                            notification.getId(), notification.getChannel(),
                            Thread.currentThread().getName(), duration, typeTag);

                    metrics.recordSuccess(notification, duration);
                } catch (Exception e) {
                    System.out.printf("SEND FAIL id=%d channel=%s error=%s%n",
                            notification.getId(), notification.getChannel(), e.getMessage());
                    metrics.recordFail(notification, e.getMessage());
                }
            });
        }

        // FIX: Shutdown MUST be outside the loop
        executor.shutdown();
        if (executor.awaitTermination(1, TimeUnit.MINUTES)) {
            this.lastMetrics = metrics;
        } else {
            System.err.println("Timeout reached before all notifications were sent.");
        }
    }


    public static class Metrics {
        private final List<Result> results = new CopyOnWriteArrayList<>();

        public void recordSuccess(Notification n, long duration) {
            results.add(new Result(n, true, duration, null));
        }

        public void recordFail(Notification n, String error) {
            results.add(new Result(n, false, 0, error));
        }

        public void printStats() {
            long total = results.size();
            long ok = results.stream().filter(r -> r.success).count();
            long fail = total - ok;

            System.out.println("=== DISPATCH REPORT ===");
            System.out.println("TOTAL: " + total + " | OK: " + ok + " | FAIL: " + fail);

            Map<String, List<Result>> byChannel = results.stream()
                    .collect(Collectors.groupingBy(r -> r.notification.getChannel()));

            byChannel.forEach((channel, list) -> {
                long okCount = list.stream().filter(r -> r.success).count();
                long failCount = list.size() - okCount;
                System.out.printf("Channel [%s]: OK-%d / FAIL-%d%n", channel, okCount, failCount);
            });
        }

        private record Result(Notification notification, boolean success, long duration, String error) {}
    }
}