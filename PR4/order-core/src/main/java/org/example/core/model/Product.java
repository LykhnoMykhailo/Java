package org.example.core.model;

import java.util.Objects;

/**
 * Base type for every product the shop can sell.
 * It is used as the upper bound of the generic order hierarchy
 * ({@code Order<T extends Product>}).
 */
public abstract class Product {

    private final String sku;
    private final String name;
    private final double price;

    protected Product(String sku, String name, double price) {
        this.sku = Objects.requireNonNull(sku, "sku");
        this.name = Objects.requireNonNull(name, "name");
        if (price < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public abstract String category();

    @Override
    public String toString() {
        return category() + "[" + sku + ", " + name + ", " + price + "]";
    }
}
