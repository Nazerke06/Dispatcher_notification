package org.qazcodenarxoz.repository;

import org.qazcodenarxoz.notification.Notification;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationRepository<T extends Notification> {
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public long nextId() {
        return idGenerator.getAndIncrement();
    }

    public void add(T notification) {
        queue.add(notification);
    }

    public List<T> peekFirst(int limit) {
        return queue.stream().limit(limit).toList();
    }

    public List<T> getAll() {
        return queue.stream().toList();
    }

    public T poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}