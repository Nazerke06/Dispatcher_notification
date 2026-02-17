package org.qazcodenarxoz.notification;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;


@RequiredArgsConstructor
@Getter
public class Notification {
    private final Long id;
    private final String channel;
    private final String to;
    private final String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        // Безопасное сравнение id
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Notification[id=%d, channel=%s, to=%s]", id, channel, to);
    }
}