package org.example.core.model;

import org.example.core.internal.OrderIdGenerator;

public final class Orders {

    private Orders() {
    }

    public static <T extends Product> Order<T> create(T product, int quantity) {
        return new Order<>(OrderIdGenerator.nextId(), product, quantity);
    }
}
