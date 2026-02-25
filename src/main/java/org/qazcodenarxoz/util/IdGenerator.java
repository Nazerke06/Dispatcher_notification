package org.qazcodenarxoz.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final AtomicLong id = new AtomicLong(1);

    public static long nextId() {
        return id.getAndIncrement();
    }
}