package org.example.core.model;

import lombok.Builder;

public final class Clothing extends Product {

    private final String size;

    @Builder
    public Clothing(String sku, String name, double price, String size) {
        super(sku, name, price);
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String category() {
        return "Clothing";
    }
}
