package org.example.processing;

import org.example.core.model.Order;
import org.example.core.model.Product;

/**
 * Called by a worker thread after an order has been processed.
 *
 * @param <T> concrete product type of the order
 */
@FunctionalInterface
public interface OrderCallback<T extends Product> {

    void onResult(Order<T> order, Throwable error);
}
