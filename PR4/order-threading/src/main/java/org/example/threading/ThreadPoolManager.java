package org.example.threading;

import org.example.threading.internal.WorkerThreadFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Thread-management module entry point. It owns an executor with a fixed
 * number of named worker threads and hides the concrete thread factory.
 */
public final class ThreadPoolManager implements AutoCloseable {

    private final ExecutorService executor;
    private final int workerCount;

    public ThreadPoolManager(String name, int workerCount) {
        if (workerCount <= 0) {
            throw new IllegalArgumentException("workerCount must be > 0");
        }
        this.workerCount = workerCount;
        this.executor = Executors.newFixedThreadPool(workerCount, new WorkerThreadFactory(name));
    }

    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    public <V> Future<V> submit(Callable<V> task) {
        return executor.submit(task);
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
