package org.example.threading;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe stock registry guarded by a read/write lock: many readers are
 * allowed, writers are exclusive.
 */
public final class StockRegistry {

    private final Map<String, Integer> stock = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void register(String sku, int quantity) {
        lock.writeLock().lock();
        try {
            stock.merge(sku, quantity, Integer::sum);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Atomically reserves stock. Returns {@code false} when there is not
     * enough quantity left.
     */
    public boolean reserve(String sku, int quantity) {
        lock.writeLock().lock();
        try {
            int available = stock.getOrDefault(sku, 0);
            if (available < quantity) {
                return false;
            }
            stock.put(sku, available - quantity);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int available(String sku) {
        lock.readLock().lock();
        try {
            return stock.getOrDefault(sku, 0);
        } finally {
            lock.readLock().unlock();
        }
    }
}
