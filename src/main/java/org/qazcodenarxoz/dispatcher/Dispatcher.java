package org.qazcodenarxoz.dispatcher;

import lombok.extern.slf4j.Slf4j;
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


@Slf4j
public class Dispatcher<T extends Notification> {

    private final NotificationRepository<T> repository;
    private final SenderRegistry registry;
    private Metrics lastMetrics;


    public Dispatcher(NotificationRepository<T> repository, SenderRegistry registry) {
        this.repository = repository;
        this.registry = registry;
    }

    public void sendAll(int threads) throws InterruptedException {
        log.info("Starting dispatch process with {} threads", threads);

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Metrics metrics = new Metrics();
        T notification;
        while ((notification = repository.poll()) != null) {
            T finalNotification = notification;
            executor.submit(() -> processTask(finalNotification, metrics));
        }
        executor.shutdown();
        if (executor.awaitTermination(1, TimeUnit.MINUTES)) {
            this.lastMetrics = metrics;
            log.info("Dispatch process finished successfully");
        } else {
            log.error("Dispatch process timed out before all tasks finished!");
        }
    }
    private void processTask(T notification, Metrics metrics) {
        long start = System.currentTimeMillis();
        try {
            Optional<Sender<?>> senderOpt = registry.getSender(notification.getChannel());

            if (senderOpt.isEmpty()) {
                throw new RuntimeException("No sender found for channel: " + notification.getChannel());
            }

            @SuppressWarnings("unchecked")
            Sender<T> sender = (Sender<T>) senderOpt.get();
            sender.send(notification);

            long duration = System.currentTimeMillis() - start;
            String typeTag = (notification instanceof OTPNotification) ? "[OTP-CONFIDENTIAL]" : "";
            System.out.println(String.format("SEND OK id=%d channel=%s duration=%dms %s",
                    notification.getId(), notification.getChannel(), duration, typeTag));

            metrics.recordSuccess(notification, duration);
        } catch (Exception e) {
            System.out.println(String.format("SEND FAIL id=%d channel=%s error=%s",
                    notification.getId(), notification.getChannel(), e.getMessage()));
            metrics.recordFail(notification, e.getMessage());
        }
    }
    public void printStats() {
        if (lastMetrics != null) {
            lastMetrics.printStats();
        } else {
            log.warn("Attempted to print stats, but no metrics are available.");
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

                // Используем LongSummaryStatistics для расчета времени
                java.util.LongSummaryStatistics timeStats = list.stream()
                        .filter(r -> r.success)
                        .mapToLong(r -> r.duration)
                        .summaryStatistics();

                System.out.printf("Channel [%s]: OK-%d / FAIL-%d | Avg: %.1fms | Max: %dms%n",
                        channel, okCount, failCount,
                        timeStats.getAverage(),
                        timeStats.getMax()
                );
            });
        }

        private record Result(Notification notification, boolean success, long duration, String error) {}
    }
}