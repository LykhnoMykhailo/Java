package org.example.threading;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe aggregation of processing results. Combines an explicit lock,
 * atomics and a concurrent map.
 */
public final class OrderStats {

    private final AtomicLong completed = new AtomicLong();
    private final AtomicLong failed = new AtomicLong();
    private final DoubleAdder revenue = new DoubleAdder();
    private final ConcurrentHashMap<String, AtomicInteger> byCategory = new ConcurrentHashMap<>();
    private final ReentrantLock mutationLock = new ReentrantLock();

    public void recordCompleted(String category, double amount) {
        mutationLock.lock();
        try {
            completed.incrementAndGet();
            revenue.add(amount);
            byCategory.computeIfAbsent(category, key -> new AtomicInteger()).incrementAndGet();
        } finally {
            mutationLock.unlock();
        }
    }

    public void recordFailed(String category) {
        mutationLock.lock();
        try {
            failed.incrementAndGet();
            byCategory.computeIfAbsent(category, key -> new AtomicInteger());
        } finally {
            mutationLock.unlock();
        }
    }

    public synchronized Snapshot snapshot() {
        var categories = new java.util.HashMap<String, Integer>();
        byCategory.forEach((key, value) -> categories.put(key, value.get()));
        return new Snapshot(completed.get(), failed.get(), revenue.sum(), Map.copyOf(categories));
    }

    public long getCompleted() {
        return completed.get();
    }

    public long getFailed() {
        return failed.get();
    }

    public record Snapshot(long completed, long failed, double revenue, Map<String, Integer> byCategory) {

        @Override
        public String toString() {
            return String.format(
                    "completed=%d, failed=%d, revenue=%.2f, byCategory=%s",
                    completed, failed, revenue, byCategory);
        }
    }
}
