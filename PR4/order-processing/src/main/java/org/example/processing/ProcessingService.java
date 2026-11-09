package org.example.processing;

import org.example.core.exception.OrderProcessingException;
import org.example.core.model.Order;
import org.example.core.model.OrderStatus;
import org.example.core.model.Product;
import org.example.threading.ThreadPoolManager;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generic, product-bounded processing service. Orders are placed on a blocking
 * queue and consumed by worker threads provided by the thread-management
 * module. An optional {@link OrderCallback} is notified from the worker thread.
 *
 * @param <T> concrete product type handled by this service
 */
public final class ProcessingService<T extends Product> implements AutoCloseable {

    private final BlockingQueue<Order<T>> queue = new LinkedBlockingQueue<>();
    private final ThreadPoolManager pool;
    private final OrderProcessor<T> processor;
    private final OrderCallback<T> callback;

    private final AtomicInteger submitted = new AtomicInteger();
    private final AtomicInteger completed = new AtomicInteger();

    private volatile boolean running = true;

    public ProcessingService(String name,
                             int workerCount,
                             OrderProcessor<T> processor,
                             OrderCallback<T> callback) {
        this.processor = Objects.requireNonNull(processor, "processor");
        this.callback = callback;
        this.pool = new ThreadPoolManager(name, workerCount);
    }

    public void start() {
        for (int i = 0; i < pool.getWorkerCount(); i++) {
            pool.submit(this::workLoop);
        }
    }

    public void submit(Order<T> order) {
        if (!running) {
            throw new IllegalStateException("Service has been stopped");
        }
        submitted.incrementAndGet();
        queue.add(order);
    }

    private void workLoop() {
        while (running || !queue.isEmpty()) {
            try {
                Order<T> order = queue.poll(100, TimeUnit.MILLISECONDS);
                if (order != null) {
                    process(order);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void process(Order<T> order) {
        String thread = Thread.currentThread().getName();
        order.setStatus(OrderStatus.PROCESSING);
        System.out.printf("[%s] start  %s %s%n", thread, order.getId(), order.getProduct().getName());
        try {
            processor.process(order);
            order.setStatus(OrderStatus.COMPLETED);
            System.out.printf("[%s] done   %s -> COMPLETED%n", thread, order.getId());
            notifyCallback(order, null);
        } catch (OrderProcessingException | RuntimeException e) {
            order.setStatus(OrderStatus.FAILED);
            order.setError(e.getMessage());
            System.out.printf("[%s] done   %s -> FAILED (%s)%n", thread, order.getId(), e.getMessage());
            notifyCallback(order, e);
        } finally {
            completed.incrementAndGet();
        }
    }

    private void notifyCallback(Order<T> order, Throwable error) {
        if (callback == null) {
            return;
        }
        try {
            callback.onResult(order, error);
        } catch (RuntimeException ignored) {
            // A broken callback must not stop the worker thread.
        }
    }

    public boolean awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        while (System.nanoTime() < deadline) {
            if (completed.get() == submitted.get() && queue.isEmpty()) {
                return true;
            }
            Thread.sleep(20);
        }
        return completed.get() == submitted.get() && queue.isEmpty();
    }

    public int getWorkerCount() {
        return pool.getWorkerCount();
    }

    @Override
    public void close() {
        running = false;
        pool.close();
    }
}
