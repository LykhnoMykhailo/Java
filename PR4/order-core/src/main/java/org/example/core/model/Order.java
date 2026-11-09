package org.example.core.model;

import java.util.Objects;

/**
 * Generic order. The type parameter is bounded so that only subclasses of
 * {@link Product} can ever become an order line.
 *
 * @param <T> concrete product type handled by this order
 */
public class Order<T extends Product> {

    private final String id;
    private final T product;
    private final int quantity;

    private volatile OrderStatus status = OrderStatus.NEW;
    private volatile String error;

    public Order(String id, T product, int quantity) {
        this.id = Objects.requireNonNull(id, "id");
        this.product = Objects.requireNonNull(product, "product");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public T getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = Objects.requireNonNull(status, "status");
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public double total() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "Order{" + id + ", " + product.getName() + " x" + quantity
                + ", total=" + total() + ", status=" + status + "}";
    }
}
