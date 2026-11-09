package org.example.processing;

import org.example.core.exception.OrderProcessingException;
import org.example.core.model.Order;
import org.example.core.model.Product;

/**
 * Functional interface describing a single order-processing step.
 *
 * @param <T> concrete product type this processor accepts
 */
@FunctionalInterface
public interface OrderProcessor<T extends Product> {

    void process(Order<T> order) throws OrderProcessingException;
}
