package org.example.processing;

import org.example.core.exception.ValidationException;
import org.example.core.model.Order;
import org.example.core.model.Product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Generic filtering helpers. The type parameter is bounded by {@link Product}
 * and the checked exception covers unsafe generic casts.
 */
public final class OrderFilters {

    private OrderFilters() {
    }

    public static <T extends Product> Predicate<Order<? extends Product>> byTypePredicate(Class<T> type) {
        return order -> type.isInstance(order.getProduct());
    }

    public static <T extends Product> List<Order<T>> byType(
            Collection<? extends Order<? extends Product>> orders,
            Class<T> type) throws ValidationException {
        List<Order<T>> result = new ArrayList<>();
        for (Order<? extends Product> order : orders) {
            if (type.isInstance(order.getProduct())) {
                try {
                    @SuppressWarnings("unchecked")
                    Order<T> typed = (Order<T>) order;
                    result.add(typed);
                } catch (ClassCastException e) {
                    throw new ValidationException(
                            "Cannot cast order " + order.getId() + " to " + type.getSimpleName(), e);
                }
            }
        }
        return result;
    }
}
