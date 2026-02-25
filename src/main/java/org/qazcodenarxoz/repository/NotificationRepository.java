package org.qazcodenarxoz.repository;

import org.qazcodenarxoz.notification.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NotificationRepository<T extends Notification> {

    private final Queue<T> queue = new ConcurrentLinkedQueue<>();

    public void add(T notification) {
        queue.add(notification);
    }

    public List<T> getAll() {
//        return new ArrayList<>(queue);
        return queue.stream().toList();
    }

    public T poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
    public Queue<T> getQueue() {
        return queue;
    }
}
