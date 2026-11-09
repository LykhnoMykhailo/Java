package org.example.threading;

import org.example.core.model.Order;
import org.example.core.model.OrderStatus;
import org.example.core.model.Product;

import java.util.Collection;

/**
 * Aggregates orders with a parallel stream (ForkJoinPool) and method
 * references.
 */
public final class ParallelAggregator {

    private ParallelAggregator() {
    }

    public static double totalRevenue(Collection<? extends Order<? extends Product>> orders) {
        return orders.parallelStream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(Order::total)
                .sum();
    }

    public static long countCompleted(Collection<? extends Order<? extends Product>> orders) {
        return orders.parallelStream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .count();
    }
}
