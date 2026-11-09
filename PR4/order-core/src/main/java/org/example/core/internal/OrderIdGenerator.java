package org.example.core.internal;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Not exported by the {@code order.core} module: other modules cannot
 * reference it directly.
 */
public final class OrderIdGenerator {

    private static final AtomicLong SEQUENCE = new AtomicLong(1);

    private OrderIdGenerator() {
    }

    public static String nextId() {
        return "ORD-" + SEQUENCE.getAndIncrement();
    }
}
