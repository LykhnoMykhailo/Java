package org.example.storage;

import org.example.core.model.Product;
import org.example.storage.internal.InMemoryOrderRepository;

/**
 * Public factory for repositories. The concrete implementation lives in the
 * unexported package {@code org.example.storage.internal} and therefore cannot
 * be referenced from other modules.
 */
public final class Repositories {

    private Repositories() {
    }

    public static <T extends Product> OrderRepository<T> inMemory() {
        return new InMemoryOrderRepository<>();
    }
}
