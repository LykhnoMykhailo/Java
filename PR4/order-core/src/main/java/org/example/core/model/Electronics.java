package org.example.core.model;

import lombok.Builder;

public final class Electronics extends Product {

    private final int warrantyMonths;

    @Builder
    public Electronics(String sku, String name, double price, int warrantyMonths) {
        super(sku, name, price);
        this.warrantyMonths = warrantyMonths;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public String category() {
        return "Electronics";
    }
}
