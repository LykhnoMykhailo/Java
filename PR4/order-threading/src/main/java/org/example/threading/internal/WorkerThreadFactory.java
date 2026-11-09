package org.example.threading.internal;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hidden thread factory: not exported by the thread-management module.
 */
public final class WorkerThreadFactory implements ThreadFactory {

    private final String prefix;
    private final AtomicInteger counter = new AtomicInteger();

    public WorkerThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, prefix + "-worker-" + counter.incrementAndGet());
        thread.setDaemon(false);
        return thread;
    }
}
