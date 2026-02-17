package org.qazcodenarxoz.metrics;

import org.qazcodenarxoz.notification.Notification;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Metrics {

    private final List<Result> results = new CopyOnWriteArrayList<>();

    private static class Result {
        Notification notification;
        boolean success;
        long duration;
        String systemId;

        public Result(Notification notification, boolean success, long duration) {
            this.notification = notification;
            this.success = success;
            this.duration = duration;
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }
    }

    public void recordSuccess(Notification n, long duration) {
        results.add(new Result(n, true, duration));
    }

    public void recordFail(Notification n, String error) {
        results.add(new Result(n, false, 0));
    }

    public void printStats() {
        long total = results.size();
        long ok = results.stream().filter(r -> r.success).count();
        long fail = total - ok;

        System.out.println("TOTAL=" + total + " OK=" + ok + " FAIL=" + fail);

        Map<String, List<Result>> byChannel =
                results.stream()
                        .collect(Collectors.groupingBy(r -> r.notification.getChannel()));

        byChannel.forEach((channel, list) -> {
            long okCount = list.stream().filter(r -> r.success).count();
            long failCount = list.size() - okCount;
            System.out.println(channel + " ok-" + okCount + "/fail-" + failCount);
        });

        double avg = results.stream()
                .filter(r -> r.success)
                .mapToLong(r -> r.duration)
                .average()
                .orElse(0.0);

        long max = results.stream()
                .mapToLong(r -> r.duration)
                .max()
                .orElse(0);

        System.out.println("AVG=" + avg + "ms MAX=" + max + "ms");
    }
}
