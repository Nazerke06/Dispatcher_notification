package org.qazcodenarxoz.notification;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;



@Getter
public class Notification {
    private final Long id;
    private final String channel;
    private final String to;
    private final String text;

    public Notification(Long id, String channel, String to, String text) {
        this.id = id;
        this.channel = channel;
        this.to = to;
        this.text = text;
    }

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
        return String.format("Notification[id=%d, channel=%s, to=%s, text=%s]", id, channel, to, text);
    }
}
