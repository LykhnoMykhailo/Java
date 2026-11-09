package org.example.storage;

import org.example.core.model.Order;
import org.example.core.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Generic storage abstraction. {@code T} is bounded by {@link Product}, so
 * only real products can be persisted.
 *
 * @param <T> concrete product type stored by this repository
 */
public interface OrderRepository<T extends Product> {

    void save(Order<T> order);

    Optional<Order<T>> findById(String id);

    List<Order<T>> findAll();

    int size();
}
