package org.example.storage.internal;

import org.example.core.model.Order;
import org.example.core.model.Product;
import org.example.storage.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Hidden implementation detail of the storage module (the package is not
 * exported by {@code module-info.java}).
 */
public final class InMemoryOrderRepository<T extends Product> implements OrderRepository<T> {

    private final ConcurrentMap<String, Order<T>> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Order<T> order) {
        storage.put(order.getId(), order);
    }

    @Override
    public Optional<Order<T>> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Order<T>> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
